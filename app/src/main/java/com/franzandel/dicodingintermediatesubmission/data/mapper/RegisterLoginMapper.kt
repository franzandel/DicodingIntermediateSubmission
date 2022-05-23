package com.franzandel.dicodingintermediatesubmission.data.mapper

import com.franzandel.dicodingintermediatesubmission.data.model.LoginRequest
import com.franzandel.dicodingintermediatesubmission.domain.model.Login
import com.franzandel.dicodingintermediatesubmission.data.model.RegisterRequest
import com.franzandel.dicodingintermediatesubmission.domain.model.Register

/**
 * Created by Franz Andel
 * on 03 May 2022.
 */

object RegisterLoginMapper {

    fun transform(registerRequest: RegisterRequest): LoginRequest {
        return with(registerRequest) {
            LoginRequest(
                email = email,
                password = password
            )
        }
    }

    fun transform(login: Login): Register {
        return with(login) {
            Register(
                error = error,
                message = message
            )
        }
    }
}
