package com.franzandel.dicodingintermediatesubmission.data.mapper

import com.franzandel.dicodingintermediatesubmission.base.model.BaseResponse
import com.franzandel.dicodingintermediatesubmission.domain.model.Register

/**
 * Created by Franz Andel
 * on 28 April 2022.
 */

object RegisterResponseMapper {
    fun transform(baseResponse: BaseResponse): Register {
        return with(baseResponse) {
            Register(
                error = error,
                message = message
            )
        }
    }
}
