package com.franzandel.dicodingintermediatesubmission.domain.usecase

import com.franzandel.dicodingintermediatesubmission.core.usecase.BaseRequestUseCase
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.data.model.LoginRequest
import com.franzandel.dicodingintermediatesubmission.domain.model.Login
import com.franzandel.dicodingintermediatesubmission.domain.repository.LoginRepository
import javax.inject.Inject

/**
 * Created by Franz Andel
 * on 28 April 2022.
 */

class LoginUseCase @Inject constructor(
    private val repository: LoginRepository,
    coroutineThread: CoroutineThread
) : BaseRequestUseCase<Result<Login>, LoginRequest>(coroutineThread) {

    override suspend fun getOperation(bodyRequest: LoginRequest): Result<Login> {
        return repository.login(bodyRequest)
    }
}
