package com.franzandel.dicodingintermediatesubmission.di.shared

import android.content.Context
import androidx.datastore.core.DataStore
import com.example.application.AuthenticationSession
import com.example.application.HomeSession
import com.franzandel.dicodingintermediatesubmission.data.local.serializer.authenticationDataStore
import com.franzandel.dicodingintermediatesubmission.data.local.serializer.homeDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Created by Franz Andel
 * on 30 May 2022.
 */

@Module
@InstallIn(ViewModelComponent::class)
object SharedModule {

    @Provides
    @ViewModelScoped
    fun provideSettingsDataStore(@ApplicationContext context: Context): DataStore<AuthenticationSession> =
        context.authenticationDataStore

    @Provides
    @ViewModelScoped
    fun provideHomeDataStore(@ApplicationContext context: Context): DataStore<HomeSession> =
        context.homeDataStore
}
