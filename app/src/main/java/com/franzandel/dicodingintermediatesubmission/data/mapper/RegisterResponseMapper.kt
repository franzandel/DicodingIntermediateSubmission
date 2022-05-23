package com.franzandel.dicodingintermediatesubmission.data.mapper

import com.franzandel.dicodingintermediatesubmission.data.model.RegisterResponse
import com.franzandel.dicodingintermediatesubmission.domain.model.Register

/**
 * Created by Franz Andel
 * on 28 April 2022.
 */

object RegisterResponseMapper {
    fun transform(registerResponse: RegisterResponse): Register {
        return with(registerResponse) {
            Register(
                error = error,
                message = message
            )
        }
    }
}
