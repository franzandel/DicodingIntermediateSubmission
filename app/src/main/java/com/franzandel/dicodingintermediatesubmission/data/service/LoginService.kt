package com.franzandel.dicodingintermediatesubmission.data.service

import com.franzandel.dicodingintermediatesubmission.data.model.request.LoginRequest
import com.franzandel.dicodingintermediatesubmission.data.model.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by Franz Andel
 * on 28 April 2022.
 */

interface LoginService {
    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>
}
