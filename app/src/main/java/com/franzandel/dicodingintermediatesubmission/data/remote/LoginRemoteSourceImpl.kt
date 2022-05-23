package com.franzandel.dicodingintermediatesubmission.data.remote

import com.franzandel.dicodingintermediatesubmission.base.model.Result
import com.franzandel.dicodingintermediatesubmission.data.model.LoginRequest
import com.franzandel.dicodingintermediatesubmission.data.model.LoginResponse
import com.franzandel.dicodingintermediatesubmission.data.service.LoginService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import kotlin.coroutines.resume

class LoginRemoteSourceImpl(private val service: LoginService): LoginRemoteSource {

    override suspend fun login(loginRequest: LoginRequest): Result<LoginResponse> {
        return suspendCancellableCoroutine { continuation ->
            service.login(loginRequest).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
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
                            val loginResponse = Gson().fromJson<LoginResponse>(
                                response.errorBody()?.charStream()?.readText(),
                                object : TypeToken<LoginResponse>() {}.type
                            )
                            Result.Error(HttpException(response), loginResponse)
                        }
                    })
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    if (continuation.isCancelled) return
                    continuation.resume(Result.Exception(t))
                }
            })
        }
    }
}

