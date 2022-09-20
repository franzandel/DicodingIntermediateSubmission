package com.franzandel.dicodingintermediatesubmission.domain.repository

import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.data.model.request.RegisterRequest
import com.franzandel.dicodingintermediatesubmission.domain.model.Register

/**
 * Created by Franz Andel
 * on 28 April 2022.
 */

interface RegisterRepository {
    suspend fun register(registerRequest: RegisterRequest): Result<Register>
}
