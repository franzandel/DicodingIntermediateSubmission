package com.franzandel.dicodingintermediatesubmission.ui.register.data.mapper

import com.franzandel.dicodingintermediatesubmission.ui.register.data.model.RegisterResponse
import com.franzandel.dicodingintermediatesubmission.ui.register.domain.model.Register

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
