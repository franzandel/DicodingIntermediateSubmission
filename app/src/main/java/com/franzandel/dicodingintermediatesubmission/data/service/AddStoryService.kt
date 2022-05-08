package com.franzandel.dicodingintermediatesubmission.data.service

import com.franzandel.dicodingintermediatesubmission.data.model.AddStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * Created by Franz Andel
 * on 07 May 2022.
 */

interface AddStoryService {
    @Multipart
    @POST("stories")
    fun uploadImage(
        @Header("Authorization") header: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<AddStoryResponse>
}
