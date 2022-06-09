package com.franzandel.dicodingintermediatesubmission.ui.addstory

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThreadImpl
import com.franzandel.dicodingintermediatesubmission.data.consts.ValidationConst
import com.franzandel.dicodingintermediatesubmission.databinding.ActivityAddStoryBinding
import com.franzandel.dicodingintermediatesubmission.ui.camerax.CameraXActivity
import com.franzandel.dicodingintermediatesubmission.utils.extension.showDefaultSnackbar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var galleryActivityResultLauncher: ActivityResultLauncher<String>

    private val viewModel: AddStoryViewModel by viewModels()
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
                showDefaultSnackbar(getString(R.string.permission_denied), Snackbar.LENGTH_SHORT)
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
