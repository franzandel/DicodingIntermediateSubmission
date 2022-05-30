package com.franzandel.dicodingintermediatesubmission.domain.usecase

import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.core.usecase.BaseRequestUseCase
import com.franzandel.dicodingintermediatesubmission.data.mapper.RegisterLoginMapper
import com.franzandel.dicodingintermediatesubmission.data.model.RegisterRequest
import com.franzandel.dicodingintermediatesubmission.domain.model.Register
import com.franzandel.dicodingintermediatesubmission.domain.repository.RegisterRepository
import javax.inject.Inject

/**
 * Created by Franz Andel
 * on 28 April 2022.
 */

class RegisterUseCase @Inject constructor(
    private val registerRepository: RegisterRepository,
    private val loginUseCase: LoginUseCase,
    coroutineThread: CoroutineThread
) : BaseRequestUseCase<Result<Register>, RegisterRequest>(coroutineThread) {

    override suspend fun getOperation(bodyRequest: RegisterRequest): Result<Register> {
        return when (val registerResult = registerRepository.register(bodyRequest)) {
            is Result.Success -> {
                when (val loginResult = loginUseCase(RegisterLoginMapper.transform(bodyRequest))) {
                    is Result.Success -> {
                        Result.Success(RegisterLoginMapper.transform(loginResult.data))
                    }
                    is Result.Error ->
                        Result.Error(loginResult.responseCode,
                            loginResult.errorData?.let {
                                RegisterLoginMapper.transform(it)
                            }
                        )
                    is Result.Exception ->
                        Result.Exception(loginResult.throwable)
                }
            }
            else -> registerResult
        }
    }
}
