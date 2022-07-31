package com.franzandel.dicodingintermediatesubmission.ui.addstory

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.data.consts.ValidationConst
import com.franzandel.dicodingintermediatesubmission.databinding.ActivityAddStoryBinding
import com.franzandel.dicodingintermediatesubmission.ui.camerax.CameraXActivity
import com.franzandel.dicodingintermediatesubmission.ui.detail.DetailActivity
import com.franzandel.dicodingintermediatesubmission.ui.detail.StoryDetail
import com.franzandel.dicodingintermediatesubmission.utils.extension.showDefaultSnackbar
import com.franzandel.dicodingintermediatesubmission.utils.geolocation.GeolocationUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class AddStoryActivity : AppCompatActivity() {

    @Inject
    lateinit var coroutineThread: CoroutineThread

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var galleryActivityResultLauncher: ActivityResultLauncher<String>
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val viewModel: AddStoryViewModel by viewModels()
    private var file: File? = null
    private var currentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.toolbar_add_story)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        initToolbar()
        initObservers()
        initListeners()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun initToolbar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun initObservers() {
        galleryActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) {
                it?.let {
                    val file = uriToFile(it, this)
                    this.file = file
                    binding.ivAddStory.setImageURI(it)
                }
            }

        viewModel.uploadImageResult.observe(this) {
            if (it.success != null) {
                showDefaultSnackbar(getString(it.success), Snackbar.LENGTH_SHORT)
                setResult(RESULT_OK)
                finish()
            }

            if (it.error != null) {
                showDefaultSnackbar(getString(it.error), Snackbar.LENGTH_LONG)
            }
        }

        viewModel.descriptionValidation.observe(this) {
            if (it != ValidationConst.FORM_VALID) {
                binding.etDescription.error = getString(it)
            }
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra(CameraXActivity.EXTRA_PHOTO_FILE) as File
            file = myFile
            Glide.with(this).load(myFile).into(binding.ivAddStory)
        }
    }

    private fun initListeners() {
        binding.apply {
            btnFromCamera.setOnClickListener {
                if (checkPermission(Manifest.permission.CAMERA)) {
                    Intent(this@AddStoryActivity, CameraXActivity::class.java).run {
                        launcherIntentCameraX.launch(this)
                    }
                } else {
                    requestPermissionLauncher.launch(
                        arrayOf(Manifest.permission.CAMERA)
                    )
                }
            }

            btnFromGallery.setOnClickListener {
                galleryActivityResultLauncher.launch(ALL_IMAGES)
            }

            cbCurrentLocation.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
                        checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    ){
                        loadLastLocation()
                    } else {
                        requestPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                } else {
                    tvCurrentLocation.isVisible = false
                }
            }

            btnUpload.setOnClickListener {
                file?.let {
                    lifecycleScope.launch(coroutineThread.background) {
                        val compressedImageFile = Compressor.compress(this@AddStoryActivity, it)
                        viewModel.uploadImage(compressedImageFile, etDescription.text.toString(), currentLocation)
                    }
                } ?: run {
                    showDefaultSnackbar(
                        getString(R.string.add_story_select_image), Snackbar.LENGTH_SHORT
                    )
                }
            }

            etDescription.setOnFocusChangeListener { _, isFocus ->
                if (!isFocus) {
                    viewModel.validateDescription(etDescription.text.toString())
                }
            }
        }
    }

    private fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createCustomTempFile(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    private val timeStamp: String = SimpleDateFormat(
        "dd_MM_yyyy",
        Locale.US
    ).format(System.currentTimeMillis())

    // Untuk kasus Intent Camera
    private fun createCustomTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLastLocation()
                }
                permissions[Manifest.permission.CAMERA] ?: false -> {
                    Intent(this@AddStoryActivity, CameraXActivity::class.java).run {
                        launcherIntentCameraX.launch(this)
                    }
                }
                else -> {
                    showDefaultSnackbar(getString(R.string.permission_denied), Snackbar.LENGTH_SHORT)
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ){
            loadLastLocation()
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun loadLastLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                lifecycleScope.launch(Dispatchers.Default) {
                    currentLocation = location
                    val currentLocation = async {
                        GeolocationUtils.getCountryState(
                            this@AddStoryActivity,
                            location.latitude,
                            location.longitude
                        )
                    }
                    withContext(Dispatchers.Main) {
                        binding.tvCurrentLocation.text = currentLocation.await()
                            ?: getString(R.string.failed_load_location)
                        binding.tvCurrentLocation.isVisible = true
                    }
                }
            } else {
                showDefaultSnackbar(
                    getString(R.string.current_location_not_found), Snackbar.LENGTH_SHORT
                )
                binding.cbCurrentLocation.isChecked = false
            }
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private const val ALL_IMAGES = "image/*"

        fun newIntent(context: Context): Intent {
            return Intent(context, AddStoryActivity::class.java)
        }
    }
}
