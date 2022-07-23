package com.franzandel.dicodingintermediatesubmission.di.home

import com.franzandel.dicodingintermediatesubmission.data.dao.HomeDao
import com.franzandel.dicodingintermediatesubmission.data.dao.RemoteKeysDao
import com.franzandel.dicodingintermediatesubmission.data.database.StoriesDatabase
import com.franzandel.dicodingintermediatesubmission.data.service.HomeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ServiceScoped
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

/**
 * Created by Franz Andel
 * on 30 May 2022.
 */

@Module
@InstallIn(ViewModelComponent::class)
object HomeModule {

    @Provides
    @ViewModelScoped
    fun provideHomeService(retrofit: Retrofit): HomeService =
        retrofit.create(HomeService::class.java)

    @Provides
    @ViewModelScoped
    fun provideHomeDao(storiesDatabase: StoriesDatabase): HomeDao =
        storiesDatabase.homeDao()

    @Provides
    @ViewModelScoped
    fun provideRemoteKeysDao(storiesDatabase: StoriesDatabase): RemoteKeysDao =
        storiesDatabase.remoteKeysDao()
}
