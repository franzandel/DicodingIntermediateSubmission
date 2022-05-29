package com.franzandel.dicodingintermediatesubmission.di.app

import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThreadImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Franz Andel
 * on 29 May 2022.
 */

@Module
@InstallIn(SingletonComponent::class)
abstract class AppAbstractModule {

    @Binds
    @Singleton
    abstract fun provideCoroutineThread(coroutineThreadImpl: CoroutineThreadImpl): CoroutineThread
}
