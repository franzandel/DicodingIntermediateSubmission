package com.franzandel.dicodingintermediatesubmission.ui.register.data.remote

import com.franzandel.dicodingintermediatesubmission.data.Result
import com.franzandel.dicodingintermediatesubmission.ui.register.data.model.RegisterRequest
import com.franzandel.dicodingintermediatesubmission.ui.register.data.model.RegisterResponse
import com.franzandel.dicodingintermediatesubmission.ui.register.data.service.RegisterService
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
        return suspendCancellableCoroutine { continuation ->
            service.register(registerRequest).enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    continuation.resumeWith(runCatching {
                        if (response.isSuccessful) {
                            val body = response.body()
                            if (body == null) {
                                Result.Exception(NullPointerException("Response body is null"))
                            } else {
                                Result.Success(body)
                            }
                        } else {
                            val loginResponse = Gson().fromJson<RegisterResponse>(
                                response.errorBody()?.charStream()?.readText(),
                                object : TypeToken<RegisterResponse>() {}.type
                            )
                            Result.Error(HttpException(response), loginResponse)
                        }
                    })
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    if (continuation.isCancelled) return
                    continuation.resume(Result.Exception(t))
                }
            })
        }
    }
}
