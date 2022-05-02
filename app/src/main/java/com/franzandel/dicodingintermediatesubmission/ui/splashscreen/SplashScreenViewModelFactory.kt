package com.franzandel.dicodingintermediatesubmission.ui.splashscreen

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
import com.franzandel.dicodingintermediatesubmission.ui.login.LoginViewModel

class SplashScreenViewModelFactory(private val applicationContext: Context) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashScreenViewModel::class.java)) {
            val coroutineThread = CoroutineThreadImpl()
            val loginRepository = LoginRepository(
                remoteSource = LoginRemoteSource(
                    RetrofitObject.retrofit.create(LoginService::class.java)
                ),
                localSource = LoginLocalSourceImpl(
                    applicationContext.settingsDataStore
                )
            )
            return SplashScreenViewModel(
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
