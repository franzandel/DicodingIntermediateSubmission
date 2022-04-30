package com.franzandel.dicodingintermediatesubmission.domain.usecase

import com.franzandel.dicodingintermediatesubmission.base.usecase.BaseRequestUseCase
import com.franzandel.dicodingintermediatesubmission.base.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.data.LoginRepository
import com.franzandel.dicodingintermediatesubmission.data.Result
import com.franzandel.dicodingintermediatesubmission.data.model.LoginRequest
import com.franzandel.dicodingintermediatesubmission.domain.model.Login

/**
 * Created by Franz Andel
 * on 28 April 2022.
 */

class LoginUseCase(
    private val repository: LoginRepository,
    coroutineThread: CoroutineThread
) : BaseRequestUseCase<Result<Login>, LoginRequest>(coroutineThread) {

    override suspend fun getOperation(bodyRequest: LoginRequest): Result<Login> {
        return repository.login(bodyRequest)
    }
}
