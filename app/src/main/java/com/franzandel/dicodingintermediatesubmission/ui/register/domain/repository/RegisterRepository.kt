package com.franzandel.dicodingintermediatesubmission.ui.register.domain.repository

import com.franzandel.dicodingintermediatesubmission.data.Result
import com.franzandel.dicodingintermediatesubmission.ui.register.data.model.RegisterRequest
import com.franzandel.dicodingintermediatesubmission.ui.register.domain.model.Register

/**
 * Created by Franz Andel
 * on 28 April 2022.
 */

interface RegisterRepository {
    suspend fun register(registerRequest: RegisterRequest): Result<Register>
}
