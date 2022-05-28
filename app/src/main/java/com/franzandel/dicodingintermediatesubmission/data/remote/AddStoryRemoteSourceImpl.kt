package com.franzandel.dicodingintermediatesubmission.data.remote

import com.franzandel.dicodingintermediatesubmission.base.model.Result
import com.franzandel.dicodingintermediatesubmission.data.model.AddStoryRequest
import com.franzandel.dicodingintermediatesubmission.data.model.AddStoryResponse
import com.franzandel.dicodingintermediatesubmission.data.service.AddStoryService
import com.franzandel.dicodingintermediatesubmission.utils.awaitResponse
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
        return service.uploadImage("Bearer $token", addStoryRequest.file, addStoryRequest.description).awaitResponse()
    }
}
