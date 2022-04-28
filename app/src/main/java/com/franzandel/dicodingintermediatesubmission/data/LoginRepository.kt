package com.franzandel.dicodingintermediatesubmission.data

import com.franzandel.dicodingintermediatesubmission.data.mapper.LoginResponseMapper
import com.franzandel.dicodingintermediatesubmission.data.model.LoggedInUser
import com.franzandel.dicodingintermediatesubmission.data.model.LoginRequest
import com.franzandel.dicodingintermediatesubmission.domain.model.Login

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

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
        dataSource.logout()
    }

    suspend fun login(loginRequest: LoginRequest): Result<Login> {
        return when (val result = dataSource.login(loginRequest)) {
            is Result.Success -> Result.Success(LoginResponseMapper.transform(result.data))
            is Result.Error -> Result.Error(result.exception, LoginResponseMapper.transform(result.errorData))
            is Result.Exception -> Result.Exception(result.throwable)
        }

//        if (result is Result.Success) {
//            setLoggedInUser(result.data)
//        }
//
//        return result
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}
