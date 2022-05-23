package com.franzandel.dicodingintermediatesubmission.domain.repository

import com.franzandel.dicodingintermediatesubmission.base.model.Result
import com.franzandel.dicodingintermediatesubmission.data.model.RegisterRequest
import com.franzandel.dicodingintermediatesubmission.domain.model.Register

/**
 * Created by Franz Andel
 * on 28 April 2022.
 */

interface RegisterRepository {
    suspend fun register(registerRequest: RegisterRequest): Result<Register>
}
