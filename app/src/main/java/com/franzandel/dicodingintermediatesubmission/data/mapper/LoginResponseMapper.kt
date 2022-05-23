package com.franzandel.dicodingintermediatesubmission.data.mapper

import com.franzandel.dicodingintermediatesubmission.data.model.LoginResponse
import com.franzandel.dicodingintermediatesubmission.domain.model.Login
import com.franzandel.dicodingintermediatesubmission.domain.model.LoginResult

/**
 * Created by Franz Andel
 * on 28 April 2022.
 */

object LoginResponseMapper {

    fun transform(loginResponse: LoginResponse): Login {
        return with(loginResponse) {
            Login(
                error = error,
                loginResult = LoginResult(
                    name = loginResult?.name.orEmpty(),
                    token = loginResult?.token.orEmpty(),
                    userId = loginResult?.userId.orEmpty(),
                ),
                message = message
            )
        }
    }
}
