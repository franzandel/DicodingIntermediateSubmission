package com.franzandel.dicodingintermediatesubmission.ui.addstory

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.data.consts.ValidationConst
import com.franzandel.dicodingintermediatesubmission.databinding.ActivityAddStoryBinding
import com.franzandel.dicodingintermediatesubmission.di.addstory.CameraResultRegistry
import com.franzandel.dicodingintermediatesubmission.di.addstory.GalleryResultRegistry
import com.franzandel.dicodingintermediatesubmission.test.EspressoIdlingResource
import com.franzandel.dicodingintermediatesubmission.ui.camerax.CameraXActivity
import com.franzandel.dicodingintermediatesubmission.utils.LocationUtils
import com.franzandel.dicodingintermediatesubmission.utils.extension.showDefaultSnackbar
import com.franzandel.dicodingintermediatesubmission.utils.geolocation.GeolocationUtils
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

    @Inject
    @CameraResultRegistry
    lateinit var cameraResultRegistry: ActivityResultRegistry

    @Inject
    @GalleryResultRegistry
    lateinit var galleryResultRegistry: ActivityResultRegistry

    @Inject
    lateinit var locationUtils: LocationUtils

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var galleryActivityResultLauncher: ActivityResultLauncher<String>
    private lateinit var launcherIntentCameraX: ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>

    private val viewModel: AddStoryViewModel by viewModels()
    private var file: File? = null
    private var currentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.toolbar_add_story)
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
        launcherIntentCameraX = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            cameraResultRegistry
        ) {
            if (it.resultCode == CAMERA_X_RESULT) {
                val photoFile =
                    it.data?.getSerializableExtra(CameraXActivity.EXTRA_PHOTO_FILE) as File
                file = photoFile
                Glide.with(this).load(photoFile).into(binding.ivAddStory)
            }
        }

        galleryActivityResultLauncher =
            registerForActivityResult(
                ActivityResultContracts.GetContent(),
                galleryResultRegistry
            ) {
                it?.let {
                    val file = uriToFile(it, this)
                    this.file = file
                    binding.ivAddStory.setImageURI(it)
                }
            }

        requestPermissionLauncher = registerForActivityResult(
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
                    showDefaultSnackbar(
                        getString(R.string.permission_denied),
                        Snackbar.LENGTH_SHORT
                    )
                }
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
            EspressoIdlingResource.decrement()
        }

        viewModel.descriptionValidation.observe(this) {
            if (it != ValidationConst.FORM_VALID) {
                binding.etDescription.error = getString(it)
            }
        }

        locationUtils.onLocationSuccess.observe(this) { location ->
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
                    binding.tvCurrentLocation.apply {
                        text = currentLocation.await() ?: getString(R.string.failed_load_location)
                        isVisible = true
                    }
                }
            }
        }

        locationUtils.onLocationFailed.observe(this) {
            showDefaultSnackbar(
                getString(R.string.current_location_not_found), Snackbar.LENGTH_SHORT
            )
            binding.cbCurrentLocation.isChecked = false
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
                    ) {
                        locationUtils.getLocation()
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
                    val description = etDescription.text.toString()
                    if (viewModel.validateDescription(description)) {
                        lifecycleScope.launch(coroutineThread.background) {
                            val compressedImageFile = try {
                                Compressor.compress(this@AddStoryActivity, it)
                            } catch (e: IllegalStateException) {
                                it
                            }

                            val addStoryRequest = viewModel.generateAddStoryRequest(
                                compressedImageFile,
                                description,
                                currentLocation
                            )
                            viewModel.uploadImage(addStoryRequest)
                        }
                        EspressoIdlingResource.increment()
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

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            locationUtils.getLocation()
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private val timeStamp: String = SimpleDateFormat(
        "dd_MM_yyyy",
        Locale.US
    ).format(System.currentTimeMillis())

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

    private fun createCustomTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private const val ALL_IMAGES = "image/*"

        fun newIntent(context: Context): Intent {
            return Intent(context, AddStoryActivity::class.java)
        }
    }
}
