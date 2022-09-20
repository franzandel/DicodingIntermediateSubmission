package com.franzandel.dicodingintermediatesubmission.data.remote

import com.franzandel.dicodingintermediatesubmission.base.model.BaseResponse
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.data.model.request.RegisterRequest
import com.franzandel.dicodingintermediatesubmission.data.service.RegisterService
import com.franzandel.dicodingintermediatesubmission.utils.awaitResponse
import javax.inject.Inject

class RegisterRemoteSourceImpl @Inject constructor(private val service: RegisterService) :
    RegisterRemoteSource {

    override suspend fun register(registerRequest: RegisterRequest): Result<BaseResponse> {
        return service.register(registerRequest).awaitResponse()
    }
}
