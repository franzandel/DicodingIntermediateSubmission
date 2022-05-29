package com.franzandel.dicodingintermediatesubmission.data.remote

import com.franzandel.dicodingintermediatesubmission.base.model.BaseResponse
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.data.model.RegisterRequest

/**
 * Created by Franz Andel
 * on 28 April 2022.
 */

interface RegisterRemoteSource {
    suspend fun register(registerRequest: RegisterRequest): Result<BaseResponse>
}
