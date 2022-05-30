package com.franzandel.dicodingintermediatesubmission.data.remote

import com.franzandel.dicodingintermediatesubmission.base.model.BaseResponse
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.data.model.AddStoryRequest
import com.franzandel.dicodingintermediatesubmission.data.service.AddStoryService
import com.franzandel.dicodingintermediatesubmission.utils.awaitResponse
import javax.inject.Inject

class AddStoryRemoteSourceImpl @Inject constructor(private val service: AddStoryService) : AddStoryRemoteSource {

    override suspend fun uploadImage(token: String, addStoryRequest: AddStoryRequest): Result<BaseResponse> {
        return service.uploadImage(
            "Bearer $token", addStoryRequest.file, addStoryRequest.description
        ).awaitResponse()
    }
}
