package com.franzandel.dicodingintermediatesubmission.data.service

import com.franzandel.dicodingintermediatesubmission.data.model.HomeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * Created by Franz Andel
 * on 02 May 2022.
 */

interface HomeService {
    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") header: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): HomeResponse

    @GET("stories")
    fun getStories(
        @Header("Authorization") header: String
    ): Call<HomeResponse>
}
