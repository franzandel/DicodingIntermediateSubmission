package com.franzandel.dicodingintermediatesubmission.di.register

import androidx.lifecycle.ViewModel
import com.franzandel.dicodingintermediatesubmission.core.annotation.ViewModelKey
import com.franzandel.dicodingintermediatesubmission.data.remote.LoginRemoteSourceImpl
import com.franzandel.dicodingintermediatesubmission.data.remote.RegisterRemoteSource
import com.franzandel.dicodingintermediatesubmission.data.remote.RegisterRemoteSourceImpl
import com.franzandel.dicodingintermediatesubmission.data.repository.RegisterRepositoryImpl
import com.franzandel.dicodingintermediatesubmission.domain.repository.RegisterRepository
import com.franzandel.dicodingintermediatesubmission.ui.register.presentation.RegisterViewModel
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
abstract class RegisterAbstractModule {

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    abstract fun provideRegisterViewModel(registerViewModel: RegisterViewModel): ViewModel

    @Binds
    @ViewModelScoped
    abstract fun provideRegisterRepository(registerRepositoryImpl: RegisterRepositoryImpl): RegisterRepository

    @Binds
    @ViewModelScoped
    abstract fun provideRegisterRemoteSource(registerRemoteSourceImpl: RegisterRemoteSourceImpl): RegisterRemoteSource
}
