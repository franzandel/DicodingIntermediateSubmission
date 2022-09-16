package com.franzandel.dicodingintermediatesubmission.di

import com.franzandel.dicodingintermediatesubmission.di.addstory.AddStoryActivityAbstractModule
import com.franzandel.dicodingintermediatesubmission.utils.LocationUtils
import com.franzandel.dicodingintermediatesubmission.utils.TestLocationUtilsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.testing.TestInstallIn

/**
 * Created by Franz Andel
 * on 09 September 2022.
 */

@Module
@TestInstallIn(
    components = [ActivityComponent::class],
    replaces = [AddStoryActivityAbstractModule::class]
)
abstract class TestAddStoryActivityAbstractModule {

    @Binds
    @ActivityScoped
    abstract fun provideLocationUtils(testLocationUtilsImpl: TestLocationUtilsImpl): LocationUtils
}
