package com.franzandel.dicodingintermediatesubmission.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.franzandel.dicodingintermediatesubmission.base.coroutine.CoroutineThreadImpl
import com.franzandel.dicodingintermediatesubmission.data.LoginRemoteSource
import com.franzandel.dicodingintermediatesubmission.data.LoginRepository
import com.franzandel.dicodingintermediatesubmission.data.service.LoginService
import com.franzandel.dicodingintermediatesubmission.data.RetrofitObject
import com.franzandel.dicodingintermediatesubmission.data.local.HomeLocalSourceImpl
import com.franzandel.dicodingintermediatesubmission.data.local.LoginLocalSourceImpl
import com.franzandel.dicodingintermediatesubmission.data.local.serializer.settingsDataStore
import com.franzandel.dicodingintermediatesubmission.data.remote.HomeRemoteSourceImpl
import com.franzandel.dicodingintermediatesubmission.data.repository.HomeRepositoryImpl
import com.franzandel.dicodingintermediatesubmission.data.service.HomeService
import com.franzandel.dicodingintermediatesubmission.domain.repository.HomeRepository
import com.franzandel.dicodingintermediatesubmission.domain.usecase.ClearStorageUseCase
import com.franzandel.dicodingintermediatesubmission.domain.usecase.GetStoriesUseCase
import com.franzandel.dicodingintermediatesubmission.domain.usecase.LoginUseCase
import com.franzandel.dicodingintermediatesubmission.ui.login.LoginViewModel

class HomeViewModelFactory(private val applicationContext: Context) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            val coroutineThread = CoroutineThreadImpl()
            val homeRepository = HomeRepositoryImpl(
                remoteSource = HomeRemoteSourceImpl(
                    RetrofitObject.retrofit.create(HomeService::class.java)
                ),
                localSource = HomeLocalSourceImpl(
                    applicationContext.settingsDataStore
                )
            )
            val loginRepository = LoginRepository(
                remoteSource = LoginRemoteSource(
                    RetrofitObject.retrofit.create(LoginService::class.java)
                ),
                localSource = LoginLocalSourceImpl(
                    applicationContext.settingsDataStore
                )
            )
            return HomeViewModel(
                getStoriesUseCase = GetStoriesUseCase(
                    homeRepository = homeRepository,
                    loginRepository = loginRepository,
                    coroutineThread = coroutineThread
                ),
                clearStorageUseCase = ClearStorageUseCase(
                    repository = homeRepository,
                    coroutineThread = coroutineThread
                ),
                coroutineThread = coroutineThread
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
