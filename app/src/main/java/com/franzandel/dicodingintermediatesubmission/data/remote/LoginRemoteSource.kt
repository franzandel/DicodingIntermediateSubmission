package com.franzandel.dicodingintermediatesubmission.data.remote

import com.franzandel.dicodingintermediatesubmission.base.model.Result
import com.franzandel.dicodingintermediatesubmission.data.model.LoginRequest
import com.franzandel.dicodingintermediatesubmission.data.model.LoginResponse

/**
 * Created by Franz Andel
 * on 23 May 2022.
 */

interface LoginRemoteSource {
    suspend fun login(loginRequest: LoginRequest): Result<LoginResponse>
}
