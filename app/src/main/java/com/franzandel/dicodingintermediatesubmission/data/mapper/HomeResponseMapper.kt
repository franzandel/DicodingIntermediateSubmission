package com.franzandel.dicodingintermediatesubmission.data.mapper

import com.franzandel.dicodingintermediatesubmission.data.model.entity.RemoteKeysEntity
import com.franzandel.dicodingintermediatesubmission.data.model.entity.StoryEntity
import com.franzandel.dicodingintermediatesubmission.data.model.response.StoryResponse
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

    fun transform(storyResponses: List<StoryResponse>?): List<Story> {
        return storyResponses?.map {
            transform(it)
        } ?: listOf()
    }

    fun transform(storyEntity: StoryEntity): Story {
        return with(storyEntity) {
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

    fun transformStoryEntities(storyResponses: List<StoryResponse>): List<StoryEntity> {
        return storyResponses.map {
            transformStoryEntity(it)
        }
    }

    private fun transformStoryEntity(storyResponse: StoryResponse): StoryEntity {
        return with(storyResponse) {
            StoryEntity(
                id,
                createdAt,
                description,
                lat,
                lon,
                name,
                photoUrl
            )
        }
    }

    fun transformRemoteKeysEntities(
        storyResponses: List<StoryResponse>,
        prevKey: Int?,
        nextKey: Int?
    ): List<RemoteKeysEntity> {
        return storyResponses.map {
            with(it) {
                RemoteKeysEntity(
                    id = id,
                    prevKey = prevKey,
                    nextKey = nextKey
                )
            }
        }
    }
}
