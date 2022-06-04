package com.franzandel.dicodingintermediatesubmission.di.widget

import com.franzandel.dicodingintermediatesubmission.data.local.HomeLocalSource
import com.franzandel.dicodingintermediatesubmission.data.local.HomeLocalSourceImpl
import com.franzandel.dicodingintermediatesubmission.data.local.LoginLocalSource
import com.franzandel.dicodingintermediatesubmission.data.local.LoginLocalSourceImpl
import com.franzandel.dicodingintermediatesubmission.data.remote.HomeRemoteSource
import com.franzandel.dicodingintermediatesubmission.data.remote.HomeRemoteSourceImpl
import com.franzandel.dicodingintermediatesubmission.data.remote.LoginRemoteSource
import com.franzandel.dicodingintermediatesubmission.data.remote.LoginRemoteSourceImpl
import com.franzandel.dicodingintermediatesubmission.data.repository.HomeRepositoryImpl
import com.franzandel.dicodingintermediatesubmission.data.repository.LoginRepositoryImpl
import com.franzandel.dicodingintermediatesubmission.domain.repository.HomeRepository
import com.franzandel.dicodingintermediatesubmission.domain.repository.LoginRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped

/**
 * Created by Franz Andel
 * on 04 June 2022.
 */

@Module
@InstallIn(ServiceComponent::class)
abstract class StoriesWidgetAbstractModule {

    @Binds
    @ServiceScoped
    abstract fun provideLoginRepository(loginRepositoryImpl: LoginRepositoryImpl): LoginRepository

    @Binds
    @ServiceScoped
    abstract fun provideLoginLocalSource(loginLocalSourceImpl: LoginLocalSourceImpl): LoginLocalSource

    @Binds
    @ServiceScoped
    abstract fun provideLoginRemoteSource(loginRemoteSourceImpl: LoginRemoteSourceImpl): LoginRemoteSource

    @Binds
    @ServiceScoped
    abstract fun provideHomeRepository(homeRepositoryImpl: HomeRepositoryImpl): HomeRepository

    @Binds
    @ServiceScoped
    abstract fun provideHomeLocalSource(homeLocalSourceImpl: HomeLocalSourceImpl): HomeLocalSource

    @Binds
    @ServiceScoped
    abstract fun provideHomeRemoteSource(homeRemoteSourceImpl: HomeRemoteSourceImpl): HomeRemoteSource
}
