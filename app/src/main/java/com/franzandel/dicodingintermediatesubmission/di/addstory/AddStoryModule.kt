package com.franzandel.dicodingintermediatesubmission.di.addstory

import com.franzandel.dicodingintermediatesubmission.data.service.AddStoryService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

/**
 * Created by Franz Andel
 * on 30 May 2022.
 */

@Module
@InstallIn(ViewModelComponent::class)
object AddStoryModule {

    @Provides
    @ViewModelScoped
    fun provideAddStoryService(retrofit: Retrofit): AddStoryService =
        retrofit.create(AddStoryService::class.java)
}
