package com.franzandel.dicodingintermediatesubmission.data.remote

import com.franzandel.dicodingintermediatesubmission.base.data.NetworkObject
import com.franzandel.dicodingintermediatesubmission.base.model.BaseResponse
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.data.model.request.AddStoryRequest
import com.franzandel.dicodingintermediatesubmission.data.service.AddStoryService
import com.franzandel.dicodingintermediatesubmission.utils.awaitResponse
import javax.inject.Inject

class AddStoryRemoteSourceImpl @Inject constructor(private val service: AddStoryService) : AddStoryRemoteSource {

    override suspend fun uploadImage(token: String, addStoryRequest: AddStoryRequest): Result<BaseResponse> {
        return with(addStoryRequest) {
            service.uploadImage(
                NetworkObject.wrapBearer(token), file, description, latitude, longitude
            ).awaitResponse()
        }
    }
}
