package com.franzandel.dicodingintermediatesubmission.test

import com.franzandel.dicodingintermediatesubmission.data.model.entity.StoryEntity
import com.franzandel.dicodingintermediatesubmission.data.model.response.HomeResponse
import com.franzandel.dicodingintermediatesubmission.data.model.response.StoryResponse
import com.franzandel.dicodingintermediatesubmission.domain.model.Story

/**
 * Created by Franz Andel
 * on 20 August 2022.
 */

object RoomUtils {

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

    fun getOneStoryEntities(): List<StoryEntity> =
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
            ),
            StoryEntity(
                createdAt = "2022-08-18T21:32:30.752Z",
                description = "2",
                id = "story-tVyHngkIXympcInp1",
                lat = null,
                lon = null,
                name = "Dstory",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1660858350751_Valb0Cqa.jpg"
            ),
            StoryEntity(
                createdAt = "2022-08-18T21:32:30.752Z",
                description = "2",
                id = "story-tVyHngkIXympcInp2",
                lat = null,
                lon = null,
                name = "Dstory",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1660858350751_Valb0Cqa.jpg"
            ),
            StoryEntity(
                createdAt = "2022-08-18T21:32:30.752Z",
                description = "2",
                id = "story-tVyHngkIXympcInp3",
                lat = null,
                lon = null,
                name = "Dstory",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1660858350751_Valb0Cqa.jpg"
            ),
            StoryEntity(
                createdAt = "2022-08-18T21:32:30.752Z",
                description = "2",
                id = "story-tVyHngkIXympcInp4",
                lat = null,
                lon = null,
                name = "Dstory",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1660858350751_Valb0Cqa.jpg"
            )
        )

    fun getStoryEntitiesWithLocation(): List<StoryEntity> =
        listOf(
            StoryEntity(
                createdAt = "2022-08-18T21:32:30.752Z",
                description = "2",
                id = "story-tVyHngkIXympcInp",
                lat = -6.4517064,
                lon = 107.9145669,
                name = "Dstory",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1660858350751_Valb0Cqa.jpg"
            ),
            StoryEntity(
                createdAt = "2022-08-18T21:32:30.752Z",
                description = "2",
                id = "story-tVyHngkIXympcInp1",
                lat = -6.4517064,
                lon = 107.9145669,
                name = "Dstory",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1660858350751_Valb0Cqa.jpg"
            ),
            StoryEntity(
                createdAt = "2022-08-18T21:32:30.752Z",
                description = "2",
                id = "story-tVyHngkIXympcInp2",
                lat = -6.4517064,
                lon = 107.9145669,
                name = "Dstory",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1660858350751_Valb0Cqa.jpg"
            ),
            StoryEntity(
                createdAt = "2022-08-18T21:32:30.752Z",
                description = "2",
                id = "story-tVyHngkIXympcInp3",
                lat = -6.4517064,
                lon = 107.9145669,
                name = "Dstory",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1660858350751_Valb0Cqa.jpg"
            ),
            StoryEntity(
                createdAt = "2022-08-18T21:32:30.752Z",
                description = "2",
                id = "story-tVyHngkIXympcInp4",
                lat = -6.4517064,
                lon = 107.9145669,
                name = "Dstory",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1660858350751_Valb0Cqa.jpg"
            )
        )
}
