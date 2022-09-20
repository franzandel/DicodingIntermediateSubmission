package com.franzandel.dicodingintermediatesubmission.di.widget

import android.content.Context
import androidx.datastore.core.DataStore
import com.example.application.AuthenticationSession
import com.example.application.HomeSession
import com.franzandel.dicodingintermediatesubmission.data.dao.HomeDao
import com.franzandel.dicodingintermediatesubmission.data.dao.RemoteKeysDao
import com.franzandel.dicodingintermediatesubmission.data.database.StoriesDatabase
import com.franzandel.dicodingintermediatesubmission.data.local.serializer.authenticationDataStore
import com.franzandel.dicodingintermediatesubmission.data.local.serializer.homeDataStore
import com.franzandel.dicodingintermediatesubmission.data.service.HomeService
import com.franzandel.dicodingintermediatesubmission.data.service.LoginService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import retrofit2.Retrofit

/**
 * Created by Franz Andel
 * on 04 June 2022.
 */

@Module
@InstallIn(ServiceComponent::class)
object StoriesWidgetModule {

    @Provides
    @ServiceScoped
    fun provideSettingsDataStore(@ApplicationContext context: Context): DataStore<AuthenticationSession> =
        context.authenticationDataStore

    @Provides
    @ServiceScoped
    fun provideHomeDataStore(@ApplicationContext context: Context): DataStore<HomeSession> =
        context.homeDataStore

    @Provides
    @ServiceScoped
    fun provideLoginService(retrofit: Retrofit): LoginService =
        retrofit.create(LoginService::class.java)

    @Provides
    @ServiceScoped
    fun provideHomeService(retrofit: Retrofit): HomeService =
        retrofit.create(HomeService::class.java)

    @Provides
    @ServiceScoped
    fun provideHomeDao(storiesDatabase: StoriesDatabase): HomeDao =
        storiesDatabase.homeDao()

    @Provides
    @ServiceScoped
    fun provideRemoteKeysDao(storiesDatabase: StoriesDatabase): RemoteKeysDao =
        storiesDatabase.remoteKeysDao()
}
