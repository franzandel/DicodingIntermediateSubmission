package com.franzandel.dicodingintermediatesubmission.data.mapper

import com.franzandel.dicodingintermediatesubmission.base.model.BaseResponse
import com.franzandel.dicodingintermediatesubmission.domain.model.AddStory

/**
 * Created by Franz Andel
 * on 07 May 2022.
 */

object AddStoryResponseMapper {

    fun transform(baseResponse: BaseResponse): AddStory {
        return with(baseResponse) {
            AddStory(
                error = error,
                message = message
            )
        }
    }
}
