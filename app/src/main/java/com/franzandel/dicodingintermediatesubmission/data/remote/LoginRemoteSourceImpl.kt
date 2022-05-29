package com.franzandel.dicodingintermediatesubmission.data.remote

import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.data.model.LoginRequest
import com.franzandel.dicodingintermediatesubmission.data.model.LoginResponse
import com.franzandel.dicodingintermediatesubmission.data.service.LoginService
import com.franzandel.dicodingintermediatesubmission.utils.awaitResponse

class LoginRemoteSourceImpl(private val service: LoginService): LoginRemoteSource {

    override suspend fun login(loginRequest: LoginRequest): Result<LoginResponse> {
        return service.login(loginRequest).awaitResponse()
    }
}

