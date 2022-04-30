package com.franzandel.dicodingintermediatesubmission.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.franzandel.dicodingintermediatesubmission.base.coroutine.CoroutineThreadImpl
import com.franzandel.dicodingintermediatesubmission.data.LoginRemoteSource
import com.franzandel.dicodingintermediatesubmission.data.LoginRepository
import com.franzandel.dicodingintermediatesubmission.data.LoginService
import com.franzandel.dicodingintermediatesubmission.data.RetrofitObject
import com.franzandel.dicodingintermediatesubmission.data.local.LoginLocalSourceImpl
import com.franzandel.dicodingintermediatesubmission.data.local.serializer.settingsDataStore
import com.franzandel.dicodingintermediatesubmission.domain.usecase.GetTokenUseCase
import com.franzandel.dicodingintermediatesubmission.domain.usecase.LoginUseCase

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class LoginViewModelFactory(private val applicationContext: Context) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            val coroutineThread = CoroutineThreadImpl()
            val loginRepository = LoginRepository(
                remoteSource = LoginRemoteSource(
                    RetrofitObject.retrofit.create(LoginService::class.java)
                ),
                localSource = LoginLocalSourceImpl(
                    applicationContext.settingsDataStore
                )
            )
            return LoginViewModel(
                loginUseCase = LoginUseCase(
                    repository = loginRepository,
                    coroutineThread = coroutineThread
                ),
                getTokenUseCase = GetTokenUseCase(
                    repository = loginRepository,
                    coroutineThread = coroutineThread
                ),
                coroutineThread = coroutineThread
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
