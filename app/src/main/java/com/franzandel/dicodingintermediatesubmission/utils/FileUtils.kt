package com.franzandel.dicodingintermediatesubmission.utils

import android.app.Application
import com.franzandel.dicodingintermediatesubmission.R
import java.io.File
import java.util.UUID

/**
 * Created by Franz Andel
 * on 10 September 2022.
 */

object FileUtils {

    // Untuk kasus CameraX
    fun createFile(application: Application): File {
        val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
            File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
        }

        val outputDirectory = if (
            mediaDir != null && mediaDir.exists()
        ) mediaDir else application.filesDir

        val file = File(outputDirectory, "${UUID.randomUUID()}.jpg")
        if (file.exists()) file.delete()
        return file
    }
}
