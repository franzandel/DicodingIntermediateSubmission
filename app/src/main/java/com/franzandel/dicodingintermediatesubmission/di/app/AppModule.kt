package com.franzandel.dicodingintermediatesubmission.di.app

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.franzandel.dicodingintermediatesubmission.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by Franz Andel
 * on 29 May 2022.
 */

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val TIMEOUT_TIME = 60L

    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                ChuckerInterceptor.Builder(context)
                    .build()
            )
            .writeTimeout(TIMEOUT_TIME, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_TIME, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT_TIME, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.STORY_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}
