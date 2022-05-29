package com.franzandel.dicodingintermediatesubmission.di.login

import androidx.lifecycle.ViewModel
import com.franzandel.dicodingintermediatesubmission.core.annotation.ViewModelKey
import com.franzandel.dicodingintermediatesubmission.data.local.LoginLocalSource
import com.franzandel.dicodingintermediatesubmission.data.local.LoginLocalSourceImpl
import com.franzandel.dicodingintermediatesubmission.data.remote.LoginRemoteSource
import com.franzandel.dicodingintermediatesubmission.data.remote.LoginRemoteSourceImpl
import com.franzandel.dicodingintermediatesubmission.data.repository.LoginRepositoryImpl
import com.franzandel.dicodingintermediatesubmission.domain.repository.LoginRepository
import com.franzandel.dicodingintermediatesubmission.ui.login.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.multibindings.IntoMap

/**
 * Created by Franz Andel
 * on 29 May 2022.
 */

@Module
@InstallIn(ViewModelComponent::class)
abstract class LoginAbstractModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun provideLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @ViewModelScoped
    abstract fun provideLoginRepository(loginRepositoryImpl: LoginRepositoryImpl): LoginRepository

    @Binds
    @ViewModelScoped
    abstract fun provideLoginLocalSource(loginLocalSourceImpl: LoginLocalSourceImpl): LoginLocalSource

    @Binds
    @ViewModelScoped
    abstract fun provideLoginRemoteSource(loginRemoteSourceImpl: LoginRemoteSourceImpl): LoginRemoteSource
}
