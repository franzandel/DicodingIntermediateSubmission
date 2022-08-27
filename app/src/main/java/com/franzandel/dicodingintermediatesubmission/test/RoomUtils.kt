package com.franzandel.dicodingintermediatesubmission.test

import com.franzandel.dicodingintermediatesubmission.data.model.HomeResponse
import com.franzandel.dicodingintermediatesubmission.data.model.StoryEntity
import com.franzandel.dicodingintermediatesubmission.data.model.StoryResponse
import com.franzandel.dicodingintermediatesubmission.domain.model.Story

/**
 * Created by Franz Andel
 * on 20 August 2022.
 */

object RoomUtils {

    const val ERROR_INSERT_TO_DB = "Error insert to DB"
    const val ERROR_DELETE_FROM_DB = "Error delete from DB"
    const val NO_DATA_FOUND = "No data found on DB"

    fun getStory(): Story =
        Story(
            createdAt = "2022-08-18T21:32:30.752Z",
            description = "2",
            id = "story-tVyHngkIXympcInp",
            latitude = null,
            longitude = null,
            name = "Dstory",
            photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1660858350751_Valb0Cqa.jpg"
        )

    fun getStories(): List<Story> =
        listOf(
            Story(
                createdAt = "2022-08-18T21:32:30.752Z",
                description = "2",
                id = "story-tVyHngkIXympcInp",
                latitude = null,
                longitude = null,
                name = "Dstory",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1660858350751_Valb0Cqa.jpg"
            )
        )

    fun getHomeResponse(): HomeResponse =
        HomeResponse(
            listOf(
                StoryResponse(
                    createdAt = "2022-08-18T21:32:30.752Z",
                    description = "2",
                    id = "story-tVyHngkIXympcInp",
                    lat = null,
                    lon = null,
                    name = "Dstory",
                    photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1660858350751_Valb0Cqa.jpg"
                )
            )
        )

    fun getEmptyHomeResponse(): HomeResponse = HomeResponse(listOf())

    fun getStoryEntities(): List<StoryEntity> =
        listOf(
            StoryEntity(
                createdAt = "2022-08-18T21:32:30.752Z",
                description = "2",
                id = "story-tVyHngkIXympcInp",
                lat = null,
                lon = null,
                name = "Dstory",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1660858350751_Valb0Cqa.jpg"
            )
        )
}
