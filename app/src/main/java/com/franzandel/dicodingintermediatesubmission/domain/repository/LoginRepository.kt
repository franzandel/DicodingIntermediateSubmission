package com.franzandel.dicodingintermediatesubmission.domain.repository

import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.data.model.request.LoginRequest
import com.franzandel.dicodingintermediatesubmission.domain.model.Login
import kotlinx.coroutines.flow.Flow

/**
 * Created by Franz Andel
 * on 23 May 2022.
 */

interface LoginRepository {
    suspend fun login(loginRequest: LoginRequest): Result<Login>
    suspend fun getToken(): Result<Flow<String>>
}
