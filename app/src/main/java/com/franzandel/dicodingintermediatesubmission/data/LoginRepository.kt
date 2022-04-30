package com.franzandel.dicodingintermediatesubmission.data

import com.franzandel.dicodingintermediatesubmission.data.local.LoginLocalSource
import com.franzandel.dicodingintermediatesubmission.data.mapper.LoginResponseMapper
import com.franzandel.dicodingintermediatesubmission.data.model.LoggedInUser
import com.franzandel.dicodingintermediatesubmission.data.model.LoginRequest
import com.franzandel.dicodingintermediatesubmission.domain.model.Login
import kotlinx.coroutines.flow.Flow

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(
    private val remoteSource: LoginRemoteSource,
    private val localSource: LoginLocalSource
) {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        remoteSource.logout()
    }

    suspend fun login(loginRequest: LoginRequest): Result<Login> {
        return when (val result = remoteSource.login(loginRequest)) {
            is Result.Success -> {
                val login = LoginResponseMapper.transform(result.data)
                localSource.saveToken(login.loginResult?.token.orEmpty())
                Result.Success(login)
            }
            is Result.Error -> Result.Error(
                result.exception,
                LoginResponseMapper.transform(result.errorData)
            )
            is Result.Exception -> Result.Exception(result.throwable)
        }
    }

    suspend fun getToken(): Result<Flow<String>> {
        return localSource.getToken()
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}
