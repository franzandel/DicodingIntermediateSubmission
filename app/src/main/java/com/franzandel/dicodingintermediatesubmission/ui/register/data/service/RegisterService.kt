package com.franzandel.dicodingintermediatesubmission.ui.register.data.service

import com.franzandel.dicodingintermediatesubmission.ui.register.data.model.RegisterRequest
import com.franzandel.dicodingintermediatesubmission.ui.register.data.model.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by Franz Andel
 * on 28 April 2022.
 */

interface RegisterService {
    @POST("register")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponse>
}
