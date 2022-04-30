package com.franzandel.dicodingintermediatesubmission.ui.register.domain.usecase

import com.franzandel.dicodingintermediatesubmission.base.usecase.BaseRequestUseCase
import com.franzandel.dicodingintermediatesubmission.base.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.data.Result
import com.franzandel.dicodingintermediatesubmission.ui.register.data.model.RegisterRequest
import com.franzandel.dicodingintermediatesubmission.ui.register.domain.model.Register
import com.franzandel.dicodingintermediatesubmission.ui.register.domain.repository.RegisterRepository

/**
 * Created by Franz Andel
 * on 28 April 2022.
 */

class RegisterUseCase(
    private val repository: RegisterRepository,
    coroutineThread: CoroutineThread
) : BaseRequestUseCase<Result<Register>, RegisterRequest>(coroutineThread) {

    override suspend fun getOperation(bodyRequest: RegisterRequest): Result<Register> {
        return repository.register(bodyRequest)
    }
}
