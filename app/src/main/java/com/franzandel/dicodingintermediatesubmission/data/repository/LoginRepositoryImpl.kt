package com.franzandel.dicodingintermediatesubmission.data.repository

import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.data.local.LoginLocalSource
import com.franzandel.dicodingintermediatesubmission.data.mapper.LoginResponseMapper
import com.franzandel.dicodingintermediatesubmission.data.model.LoginRequest
import com.franzandel.dicodingintermediatesubmission.data.remote.LoginRemoteSource
import com.franzandel.dicodingintermediatesubmission.domain.model.Login
import com.franzandel.dicodingintermediatesubmission.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val remoteSource: LoginRemoteSource,
    private val localSource: LoginLocalSource
) : LoginRepository {

    override suspend fun login(loginRequest: LoginRequest): Result<Login> {
        return when (val result = remoteSource.login(loginRequest)) {
            is Result.Success -> {
                val login = LoginResponseMapper.transform(result.data)
                localSource.saveToken(login.loginResult?.token.orEmpty())
                Result.Success(login)
            }
            is Result.Error -> {
                Result.Error(
                    result.responseCode,
                    result.errorData?.let {
                        LoginResponseMapper.transform(it)
                    }
                )
            }
            is Result.Exception -> result
        }
    }

    override suspend fun getToken(): Result<Flow<String>> {
        return localSource.getToken()
    }
}
