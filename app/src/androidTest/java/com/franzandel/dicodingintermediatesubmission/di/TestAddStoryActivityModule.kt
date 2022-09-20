package com.franzandel.dicodingintermediatesubmission.di

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.di.addstory.AddStoryActivityModule
import com.franzandel.dicodingintermediatesubmission.di.addstory.annotation.CameraResultRegistry
import com.franzandel.dicodingintermediatesubmission.di.addstory.annotation.GalleryResultRegistry
import com.franzandel.dicodingintermediatesubmission.ui.addstory.AddStoryActivity
import com.franzandel.dicodingintermediatesubmission.ui.camerax.CameraXActivity
import com.franzandel.dicodingintermediatesubmission.utils.FileUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.testing.TestInstallIn
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by Franz Andel
 * on 09 September 2022.
 */

@Module
@TestInstallIn(
    components = [ActivityComponent::class],
    replaces = [AddStoryActivityModule::class]
)
object TestAddStoryActivityModule {

    private fun saveBitmapToFile(context: Context, bitmap: Bitmap?): File? {
        val imageFile = FileUtils.createFile((context as Activity).application)
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(imageFile)
            bitmap?.compress(CompressFormat.PNG, 100, fos)
            fos.close()
            return imageFile
        } catch (e: IOException) {
            if (fos != null) {
                try {
                    fos.close()
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
            }
        }
        return null
    }

    private fun createImageCaptureActivityResultStub(context: Context): ActivityResult {
        val bitmap =
            ContextCompat.getDrawable(context, R.drawable.ic_launcher_background)?.toBitmap()
        val photoFile = saveBitmapToFile(context, bitmap)
        val intent = Intent().apply {
            putExtra(CameraXActivity.EXTRA_PHOTO_FILE, photoFile)
        }

        return ActivityResult(AddStoryActivity.CAMERA_X_RESULT, intent)
    }

    @Provides
    @ActivityScoped
    @CameraResultRegistry
    fun provideCameraActivityResultRegistry(@ActivityContext context: Context): ActivityResultRegistry {
        return object : ActivityResultRegistry() {
            override fun <I, O> onLaunch(
                requestCode: Int,
                contract: ActivityResultContract<I, O>,
                input: I,
                options: ActivityOptionsCompat?
            ) {
                dispatchResult(requestCode, createImageCaptureActivityResultStub(context))
            }
        }
    }

    @Provides
    @ActivityScoped
    @GalleryResultRegistry
    fun provideGalleryActivityResultRegistry(@ActivityContext context: Context): ActivityResultRegistry {
        val expectedResult = Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(context.resources.getResourcePackageName(R.drawable.ic_launcher_background))
            .appendPath(context.resources.getResourceTypeName(R.drawable.ic_launcher_background))
            .appendPath(context.resources.getResourceEntryName(R.drawable.ic_launcher_background))
            .build()

        return object : ActivityResultRegistry() {
            override fun <I, O> onLaunch(
                requestCode: Int,
                contract: ActivityResultContract<I, O>,
                input: I,
                options: ActivityOptionsCompat?
            ) {
                dispatchResult(requestCode, expectedResult)
            }
        }
    }
}
