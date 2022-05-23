package com.franzandel.dicodingintermediatesubmission.data.mapper

import com.franzandel.dicodingintermediatesubmission.data.model.StoryResponse
import com.franzandel.dicodingintermediatesubmission.domain.model.Story

/**
 * Created by Franz Andel
 * on 02 May 2022.
 */

object HomeResponseMapper {

    fun transform(storyResponse: StoryResponse): Story {
        return with(storyResponse) {
            Story(
                createdAt = createdAt,
                description = description,
                id = id,
                latitude = lat,
                longitude = lon,
                name = name,
                photoUrl = photoUrl
            )
        }
    }

    fun transform(storyResponses: List<StoryResponse>): List<Story> {
        return storyResponses.map {
            transform(it)
        }
    }
}
