package com.franzandel.dicodingintermediatesubmission.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by Franz Andel
 * on 28 April 2022.
 */

object RetrofitObject {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://story-api.dicoding.dev/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
