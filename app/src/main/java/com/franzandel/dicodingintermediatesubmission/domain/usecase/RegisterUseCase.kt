package com.franzandel.dicodingintermediatesubmission.domain.usecase

import com.franzandel.dicodingintermediatesubmission.base.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.base.usecase.BaseRequestUseCase
import com.franzandel.dicodingintermediatesubmission.base.model.Result
import com.franzandel.dicodingintermediatesubmission.data.mapper.RegisterLoginMapper
import com.franzandel.dicodingintermediatesubmission.domain.repository.LoginRepository
import com.franzandel.dicodingintermediatesubmission.data.model.RegisterRequest
import com.franzandel.dicodingintermediatesubmission.domain.model.Register
import com.franzandel.dicodingintermediatesubmission.domain.repository.RegisterRepository

/**
 * Created by Franz Andel
 * on 28 April 2022.
 */

class RegisterUseCase(
    private val registerRepository: RegisterRepository,
    private val loginRepository: LoginRepository,
    coroutineThread: CoroutineThread
) : BaseRequestUseCase<Result<Register>, RegisterRequest>(coroutineThread) {

    override suspend fun getOperation(bodyRequest: RegisterRequest): Result<Register> {
        return when (val registerResult = registerRepository.register(bodyRequest)) {
            is Result.Success -> {
                when (val loginResult =
                    loginRepository.login(RegisterLoginMapper.transform(bodyRequest))) {
                    is Result.Success -> {
                        Result.Success(RegisterLoginMapper.transform(loginResult.data))
                    }
                    is Result.Error -> Result.Error(loginResult.exception)
                    is Result.Exception -> Result.Exception(loginResult.throwable)
                }
            }
            else -> registerResult
        }
    }
}
