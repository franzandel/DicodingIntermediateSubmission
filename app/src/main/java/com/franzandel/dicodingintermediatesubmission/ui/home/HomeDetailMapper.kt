package com.franzandel.dicodingintermediatesubmission.ui.home

import com.franzandel.dicodingintermediatesubmission.domain.model.Story
import com.franzandel.dicodingintermediatesubmission.ui.detail.StoryDetail

/**
 * Created by Franz Andel
 * on 04 May 2022.
 */

object HomeDetailMapper {

    fun transform(story: Story): StoryDetail {
        return with(story) {
            StoryDetail(
                createdAt = createdAt,
                description = description,
                id = id,
                lat = lat,
                lon = lon,
                name = name,
                photoUrl = photoUrl
            )
        }
    }
}
