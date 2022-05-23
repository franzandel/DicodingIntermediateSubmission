package com.franzandel.dicodingintermediatesubmission.ui.register.data.remote

import com.franzandel.dicodingintermediatesubmission.base.model.Result
import com.franzandel.dicodingintermediatesubmission.ui.register.data.model.RegisterRequest
import com.franzandel.dicodingintermediatesubmission.ui.register.data.model.RegisterResponse

/**
 * Created by Franz Andel
 * on 28 April 2022.
 */

interface RegisterRemoteSource {
    suspend fun register(registerRequest: RegisterRequest): Result<RegisterResponse>
}
