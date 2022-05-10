package com.franzandel.dicodingintermediatesubmission.ui.addstory

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.base.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.base.coroutine.CoroutineThreadImpl
import com.franzandel.dicodingintermediatesubmission.databinding.ActivityAddStoryBinding
import com.franzandel.dicodingintermediatesubmission.ui.camerax.CameraXActivity
import id.zelory.compressor.Compressor
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Locale

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var galleryActivityResultLauncher: ActivityResultLauncher<String>

    private val viewModel: AddStoryViewModel by viewModels {
        AddStoryViewModelFactory(applicationContext)
    }
    private var file: File? = null

    private val coroutineThread: CoroutineThread = CoroutineThreadImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.toolbar_add_story)
        initObservers()
        initListeners()
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
                Toast.makeText(applicationContext, it.success, Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            }

            if (it.error != null) {
                Toast.makeText(applicationContext, it.error, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.descriptionValidation.observe(this) {
            if (it != AddStoryViewModel.FORM_VALID) {
                binding.etDescription.error = getString(it)
            }
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            file = myFile
            Glide.with(this).load(myFile).into(binding.ivAddStory)
        }
    }

    private fun initListeners() {
        binding.apply {
            btnFromCamera.setOnClickListener {
                if (!allPermissionsGranted()) {
                    ActivityCompat.requestPermissions(
                        this@AddStoryActivity,
                        REQUIRED_PERMISSIONS,
                        REQUEST_CODE_PERMISSIONS
                    )
                } else {
                    val intent = Intent(this@AddStoryActivity, CameraXActivity::class.java)
                    launcherIntentCameraX.launch(intent)
                }
            }

            btnFromGallery.setOnClickListener {
                galleryActivityResultLauncher.launch("image/*")
            }

            btnUpload.setOnClickListener {
                file?.let {
                    lifecycleScope.launch(coroutineThread.background) {
                        val compressedImageFile = Compressor.compress(this@AddStoryActivity, it)
                        viewModel.uploadImage(compressedImageFile, etDescription.text.toString())
                    }
                } ?: run {
                    Toast.makeText(
                        this@AddStoryActivity,
                        getString(R.string.add_story_select_image),
                        Toast.LENGTH_SHORT
                    ).show()
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

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            } else {
                val intent = Intent(this@AddStoryActivity, CameraXActivity::class.java)
                launcherIntentCameraX.launch(intent)
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
}
