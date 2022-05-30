package com.franzandel.dicodingintermediatesubmission.di.home

import androidx.lifecycle.ViewModel
import com.franzandel.dicodingintermediatesubmission.core.annotation.ViewModelKey
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
import com.franzandel.dicodingintermediatesubmission.ui.home.HomeViewModel
import com.franzandel.dicodingintermediatesubmission.ui.login.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.multibindings.IntoMap

/**
 * Created by Franz Andel
 * on 30 May 2022.
 */

@Module
@InstallIn(ViewModelComponent::class)
abstract class HomeAbstractModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun provideHomeViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    @ViewModelScoped
    abstract fun provideHomeRepository(homeRepositoryImpl: HomeRepositoryImpl): HomeRepository

    @Binds
    @ViewModelScoped
    abstract fun provideHomeLocalSource(homeLocalSourceImpl: HomeLocalSourceImpl): HomeLocalSource

    @Binds
    @ViewModelScoped
    abstract fun provideHomeRemoteSource(homeRemoteSourceImpl: HomeRemoteSourceImpl): HomeRemoteSource
}
