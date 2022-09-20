package com.franzandel.dicodingintermediatesubmission.data.remote

import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.data.model.request.LoginRequest
import com.franzandel.dicodingintermediatesubmission.data.model.response.LoginResponse

/**
 * Created by Franz Andel
 * on 23 May 2022.
 */

interface LoginRemoteSource {
    suspend fun login(loginRequest: LoginRequest): Result<LoginResponse>
}
