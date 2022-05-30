package com.franzandel.dicodingintermediatesubmission.di.register

import com.franzandel.dicodingintermediatesubmission.data.service.RegisterService
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
object RegisterModule {

    @Provides
    @ViewModelScoped
    fun provideRegisterService(retrofit: Retrofit): RegisterService =
        retrofit.create(RegisterService::class.java)
}
