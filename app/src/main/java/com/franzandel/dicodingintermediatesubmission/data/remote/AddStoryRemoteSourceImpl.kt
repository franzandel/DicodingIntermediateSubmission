package com.franzandel.dicodingintermediatesubmission.data.remote

import com.franzandel.dicodingintermediatesubmission.data.Result
import com.franzandel.dicodingintermediatesubmission.data.model.AddStoryRequest
import com.franzandel.dicodingintermediatesubmission.data.model.AddStoryResponse
import com.franzandel.dicodingintermediatesubmission.data.service.AddStoryService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import kotlin.coroutines.resume

class AddStoryRemoteSourceImpl(private val service: AddStoryService) : AddStoryRemoteSource {

    override suspend fun uploadImage(token: String, addStoryRequest: AddStoryRequest): Result<AddStoryResponse> {
        return suspendCancellableCoroutine { continuation ->
            service.uploadImage("Bearer $token", addStoryRequest.file, addStoryRequest.description)
                .enqueue(object : Callback<AddStoryResponse> {
                    override fun onResponse(
                        call: Call<AddStoryResponse>,
                        response: Response<AddStoryResponse>
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
                                val loginResponse = Gson().fromJson<AddStoryResponse>(
                                    response.errorBody()?.charStream()?.readText(),
                                    object : TypeToken<AddStoryResponse>() {}.type
                                )
                                Result.Error(HttpException(response), loginResponse)
                            }
                        })
                    }

                    override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                        if (continuation.isCancelled) return
                        continuation.resume(Result.Exception(t))
                    }
                })
        }
    }
}
