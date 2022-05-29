package com.franzandel.dicodingintermediatesubmission.ui.addstory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThreadImpl
import com.franzandel.dicodingintermediatesubmission.data.remote.LoginRemoteSourceImpl
import com.franzandel.dicodingintermediatesubmission.data.repository.LoginRepositoryImpl
import com.franzandel.dicodingintermediatesubmission.data.RetrofitObject
import com.franzandel.dicodingintermediatesubmission.data.local.LoginLocalSourceImpl
import com.franzandel.dicodingintermediatesubmission.data.local.serializer.settingsDataStore
import com.franzandel.dicodingintermediatesubmission.data.remote.AddStoryRemoteSourceImpl
import com.franzandel.dicodingintermediatesubmission.data.repository.AddStoryRepositoryImpl
import com.franzandel.dicodingintermediatesubmission.data.service.AddStoryService
import com.franzandel.dicodingintermediatesubmission.data.service.LoginService
import com.franzandel.dicodingintermediatesubmission.domain.repository.LoginRepository
import com.franzandel.dicodingintermediatesubmission.domain.usecase.GetTokenUseCase
import com.franzandel.dicodingintermediatesubmission.domain.usecase.UploadImageUseCase

class AddStoryViewModelFactory(private val applicationContext: Context) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            val coroutineThread = CoroutineThreadImpl()
            val loginRepository: LoginRepository = LoginRepositoryImpl(
                remoteSource = LoginRemoteSourceImpl(
                    RetrofitObject.retrofit.create(LoginService::class.java)
                ),
                localSource = LoginLocalSourceImpl(
                    applicationContext.settingsDataStore
                )
            )
            return AddStoryViewModel(
                uploadImageUseCase = UploadImageUseCase(
                    repository = AddStoryRepositoryImpl(
                        AddStoryRemoteSourceImpl(
                            RetrofitObject.retrofit.create(AddStoryService::class.java)
                        )
                    ),
                    getTokenUseCase = GetTokenUseCase(
                        repository = loginRepository,
                        coroutineThread = coroutineThread
                    ),
                    coroutineThread = coroutineThread
                ),
                coroutineThread = coroutineThread
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
