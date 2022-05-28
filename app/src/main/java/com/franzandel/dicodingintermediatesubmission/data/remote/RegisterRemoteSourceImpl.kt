package com.franzandel.dicodingintermediatesubmission.data.remote

import com.franzandel.dicodingintermediatesubmission.base.model.Result
import com.franzandel.dicodingintermediatesubmission.data.model.RegisterRequest
import com.franzandel.dicodingintermediatesubmission.data.model.RegisterResponse
import com.franzandel.dicodingintermediatesubmission.data.service.RegisterService
import com.franzandel.dicodingintermediatesubmission.utils.awaitResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import kotlin.coroutines.resume

class RegisterRemoteSourceImpl(private val service: RegisterService) : RegisterRemoteSource {

    override suspend fun register(registerRequest: RegisterRequest): Result<RegisterResponse> {
        return service.register(registerRequest).awaitResponse()
    }
}
