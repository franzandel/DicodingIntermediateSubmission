package com.franzandel.dicodingintermediatesubmission.data.service

import com.franzandel.dicodingintermediatesubmission.base.model.BaseResponse
import com.franzandel.dicodingintermediatesubmission.data.model.RegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by Franz Andel
 * on 28 April 2022.
 */

interface RegisterService {
    @POST("register")
    fun register(@Body registerRequest: RegisterRequest): Call<BaseResponse>
}
