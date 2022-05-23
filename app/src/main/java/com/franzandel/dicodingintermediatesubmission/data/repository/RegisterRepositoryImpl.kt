package com.franzandel.dicodingintermediatesubmission.data.repository

import com.franzandel.dicodingintermediatesubmission.base.model.Result
import com.franzandel.dicodingintermediatesubmission.data.mapper.RegisterResponseMapper
import com.franzandel.dicodingintermediatesubmission.data.model.RegisterRequest
import com.franzandel.dicodingintermediatesubmission.data.remote.RegisterRemoteSource
import com.franzandel.dicodingintermediatesubmission.domain.model.Register
import com.franzandel.dicodingintermediatesubmission.domain.repository.RegisterRepository

class RegisterRepositoryImpl(
    private val remoteSource: RegisterRemoteSource
) : RegisterRepository {

    override suspend fun register(registerRequest: RegisterRequest): Result<Register> {
        return when (val result = remoteSource.register(registerRequest)) {
            is Result.Success -> Result.Success(RegisterResponseMapper.transform(result.data))
            is Result.Error -> Result.Error(
                result.exception,
                result.errorData?.let {
                    RegisterResponseMapper.transform(it)
                }
            )
            is Result.Exception -> Result.Exception(result.throwable)
        }
    }
}
