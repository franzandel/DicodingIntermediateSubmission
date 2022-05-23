package com.franzandel.dicodingintermediatesubmission.widget

import android.content.Intent
import android.widget.RemoteViewsService
import com.franzandel.dicodingintermediatesubmission.base.coroutine.CoroutineThreadImpl
import com.franzandel.dicodingintermediatesubmission.data.remote.LoginRemoteSource
import com.franzandel.dicodingintermediatesubmission.data.repository.LoginRepository
import com.franzandel.dicodingintermediatesubmission.data.RetrofitObject
import com.franzandel.dicodingintermediatesubmission.data.local.HomeLocalSourceImpl
import com.franzandel.dicodingintermediatesubmission.data.local.LoginLocalSourceImpl
import com.franzandel.dicodingintermediatesubmission.data.local.serializer.settingsDataStore
import com.franzandel.dicodingintermediatesubmission.data.remote.HomeRemoteSourceImpl
import com.franzandel.dicodingintermediatesubmission.data.repository.HomeRepositoryImpl
import com.franzandel.dicodingintermediatesubmission.data.service.HomeService
import com.franzandel.dicodingintermediatesubmission.data.service.LoginService
import com.franzandel.dicodingintermediatesubmission.domain.usecase.GetStoriesUseCase

/**
 * Created by Franz Andel
 * on 14 May 2022.
 */

class StoriesWidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        val coroutineThread = CoroutineThreadImpl()
        return StoriesRemoteViewsFactory(
            context = applicationContext,
            getStoriesUseCase = GetStoriesUseCase(
                homeRepository = HomeRepositoryImpl(
                    remoteSource = HomeRemoteSourceImpl(
                        RetrofitObject.retrofit.create(HomeService::class.java)
                    ),
                    localSource = HomeLocalSourceImpl(
                        applicationContext.settingsDataStore
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
        )
    }
}
