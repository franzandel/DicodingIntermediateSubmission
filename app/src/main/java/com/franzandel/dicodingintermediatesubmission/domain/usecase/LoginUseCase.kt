package com.franzandel.dicodingintermediatesubmission.domain.usecase

import com.franzandel.dicodingintermediatesubmission.base.usecase.BaseRequestUseCase
import com.franzandel.dicodingintermediatesubmission.base.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.data.repository.LoginRepositoryImpl
import com.franzandel.dicodingintermediatesubmission.base.model.Result
import com.franzandel.dicodingintermediatesubmission.data.model.LoginRequest
import com.franzandel.dicodingintermediatesubmission.domain.model.Login
import com.franzandel.dicodingintermediatesubmission.domain.repository.LoginRepository

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
