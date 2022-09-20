package com.franzandel.dicodingintermediatesubmission.di.addstory

import com.franzandel.dicodingintermediatesubmission.utils.location.LocationUtils
import com.franzandel.dicodingintermediatesubmission.utils.location.LocationUtilsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

/**
 * Created by Franz Andel
 * on 16 Sep 2022.
 */

@Module
@InstallIn(ActivityComponent::class)
abstract class AddStoryActivityAbstractModule {

    @Binds
    @ActivityScoped
    abstract fun provideLocationUtils(locationUtilsImpl: LocationUtilsImpl): LocationUtils
}
