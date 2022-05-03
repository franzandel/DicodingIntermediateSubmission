package com.franzandel.dicodingintermediatesubmission.data

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.franzandel.dicodingintermediatesubmission.ThisApp
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by Franz Andel
 * on 28 April 2022.
 */

object RetrofitObject {

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(
            ChuckerInterceptor.Builder(ThisApp.appContext)
                .build()
        )
        .build()

    val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://story-api.dicoding.dev/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
