package com.franzandel.dicodingintermediatesubmission.data.mapper

import com.franzandel.dicodingintermediatesubmission.data.model.AddStoryResponse
import com.franzandel.dicodingintermediatesubmission.domain.model.AddStory

/**
 * Created by Franz Andel
 * on 07 May 2022.
 */

object AddStoryResponseMapper {

    fun transform(addStoryResponse: AddStoryResponse): AddStory {
        return with(addStoryResponse) {
            AddStory(
                error = error,
                message = message
            )
        }
    }
}
