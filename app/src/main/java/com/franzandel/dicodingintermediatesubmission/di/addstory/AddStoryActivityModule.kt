package com.franzandel.dicodingintermediatesubmission.di.addstory

import android.content.Context
import androidx.activity.result.ActivityResultRegistry
import androidx.appcompat.app.AppCompatActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

/**
 * Created by Franz Andel
 * on 30 May 2022.
 */

@Module
@InstallIn(ActivityComponent::class)
object AddStoryActivityModule {

    @Provides
    @ActivityScoped
    @CameraResultRegistry
    fun provideCameraActivityResultRegistry(@ActivityContext context: Context): ActivityResultRegistry =
        (context as AppCompatActivity).activityResultRegistry

    @Provides
    @ActivityScoped
    @GalleryResultRegistry
    fun provideGalleryActivityResultRegistry(@ActivityContext context: Context): ActivityResultRegistry =
        (context as AppCompatActivity).activityResultRegistry
}
