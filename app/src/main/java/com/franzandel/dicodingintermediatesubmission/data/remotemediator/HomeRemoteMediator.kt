package com.franzandel.dicodingintermediatesubmission.data.remotemediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.franzandel.dicodingintermediatesubmission.data.database.StoriesDatabase
import com.franzandel.dicodingintermediatesubmission.data.model.HomeEntity
import com.franzandel.dicodingintermediatesubmission.data.model.StoryEntity
import com.franzandel.dicodingintermediatesubmission.data.service.HomeService

/**
 * Created by Franz Andel <franz.andel@ovo.id>
 * on 09 July 2022.
 */

@ExperimentalPagingApi
class HomeRemoteMediator(
    private val database: StoriesDatabase,
    private val apiService: HomeService
) : RemoteMediator<Int, StoryEntity>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryEntity>
    ): MediatorResult {
        val page = INITIAL_PAGE_INDEX
        try {
            val responseData = apiService.getStories("", page, state.config.pageSize)
            val endOfPaginationReached = responseData.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.homeDao().deleteAll()
                }
                val homeEntity = HomeEntity(
                    responseData.listStory.map {
                        StoryEntity(
                            it.id,
                            it.createdAt,
                            it.description,
                            it.lat,
                            it.lon,
                            it.name,
                            it.photoUrl
                        )
                    }
                )
                database.homeDao().insertAll(homeEntity)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }
}
