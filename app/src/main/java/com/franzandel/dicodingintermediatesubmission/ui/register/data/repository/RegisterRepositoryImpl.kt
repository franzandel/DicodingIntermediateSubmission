package com.franzandel.dicodingintermediatesubmission.ui.register.data.repository

import com.franzandel.dicodingintermediatesubmission.data.Result
import com.franzandel.dicodingintermediatesubmission.ui.register.data.remote.RegisterRemoteSource
import com.franzandel.dicodingintermediatesubmission.ui.register.data.mapper.RegisterResponseMapper
import com.franzandel.dicodingintermediatesubmission.ui.register.data.model.RegisterRequest
import com.franzandel.dicodingintermediatesubmission.ui.register.domain.model.Register
import com.franzandel.dicodingintermediatesubmission.ui.register.domain.repository.RegisterRepository

class RegisterRepositoryImpl(
    private val remoteDataSource: RegisterRemoteSource
) : RegisterRepository {

    override suspend fun register(registerRequest: RegisterRequest): Result<Register> {
        return when (val result = remoteDataSource.register(registerRequest)) {
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
