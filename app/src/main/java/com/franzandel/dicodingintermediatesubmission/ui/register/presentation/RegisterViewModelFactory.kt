package com.franzandel.dicodingintermediatesubmission.ui.register.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.franzandel.dicodingintermediatesubmission.base.coroutine.CoroutineThreadImpl
import com.franzandel.dicodingintermediatesubmission.data.remote.LoginRemoteSource
import com.franzandel.dicodingintermediatesubmission.data.repository.LoginRepository
import com.franzandel.dicodingintermediatesubmission.data.RetrofitObject
import com.franzandel.dicodingintermediatesubmission.data.local.LoginLocalSourceImpl
import com.franzandel.dicodingintermediatesubmission.data.local.serializer.settingsDataStore
import com.franzandel.dicodingintermediatesubmission.data.service.LoginService
import com.franzandel.dicodingintermediatesubmission.ui.register.data.remote.RegisterRemoteSourceImpl
import com.franzandel.dicodingintermediatesubmission.ui.register.data.repository.RegisterRepositoryImpl
import com.franzandel.dicodingintermediatesubmission.ui.register.data.service.RegisterService
import com.franzandel.dicodingintermediatesubmission.ui.register.domain.usecase.RegisterUseCase

class RegisterViewModelFactory(private val applicationContext: Context) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            val coroutineThread = CoroutineThreadImpl()
            return RegisterViewModel(
                useCase = RegisterUseCase(
                    registerRepository = RegisterRepositoryImpl(
                        remoteSource = RegisterRemoteSourceImpl(
                            RetrofitObject.retrofit.create(RegisterService::class.java)
                        )
                    ),
                    loginRepository = LoginRepository(
                        remoteSource = LoginRemoteSource(
                            RetrofitObject.retrofit.create(LoginService::class.java)
                        ),
                        localSource = LoginLocalSourceImpl(
                            applicationContext.settingsDataStore
                        )
                    ),
                    coroutineThread = coroutineThread
                ),
                coroutineThread = coroutineThread
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
