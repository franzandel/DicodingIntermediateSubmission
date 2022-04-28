package com.franzandel.dicodingintermediatesubmission.ui.register.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.franzandel.dicodingintermediatesubmission.base.coroutine.CoroutineThreadImpl
import com.franzandel.dicodingintermediatesubmission.data.LoginDataSource
import com.franzandel.dicodingintermediatesubmission.data.LoginRepository
import com.franzandel.dicodingintermediatesubmission.data.RetrofitObject
import com.franzandel.dicodingintermediatesubmission.domain.LoginUseCase
import com.franzandel.dicodingintermediatesubmission.ui.login.LoginViewModel
import com.franzandel.dicodingintermediatesubmission.ui.register.data.remote.RegisterRemoteSourceImpl
import com.franzandel.dicodingintermediatesubmission.ui.register.data.repository.RegisterRepositoryImpl
import com.franzandel.dicodingintermediatesubmission.ui.register.data.service.RegisterService
import com.franzandel.dicodingintermediatesubmission.ui.register.domain.model.Register
import com.franzandel.dicodingintermediatesubmission.ui.register.domain.usecase.RegisterUseCase

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class RegisterViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            val coroutineThread = CoroutineThreadImpl()
            return RegisterViewModel(
                useCase = RegisterUseCase(
                    repository = RegisterRepositoryImpl(
                        remoteDataSource = RegisterRemoteSourceImpl(
                            RetrofitObject.retrofit.create(RegisterService::class.java)
                        )
                    ) ,
                    coroutineThread = coroutineThread
                ),
                coroutineThread = coroutineThread
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
