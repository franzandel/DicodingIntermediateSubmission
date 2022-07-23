package com.franzandel.dicodingintermediatesubmission.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.franzandel.dicodingintermediatesubmission.base.data.NetworkObject
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.data.consts.PaginationConst
import com.franzandel.dicodingintermediatesubmission.data.database.StoriesDatabase
import com.franzandel.dicodingintermediatesubmission.data.mapper.HomeResponseMapper
import com.franzandel.dicodingintermediatesubmission.data.model.HomeResponse
import com.franzandel.dicodingintermediatesubmission.data.remotemediator.HomeRemoteMediator
import com.franzandel.dicodingintermediatesubmission.data.service.HomeService
import com.franzandel.dicodingintermediatesubmission.domain.model.Story
import com.franzandel.dicodingintermediatesubmission.utils.awaitResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomeRemoteSourceImpl @Inject constructor(
    private val database: StoriesDatabase,
    private val service: HomeService
) : HomeRemoteSource {

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getPagingStories(token: String, location: Int): Result<Flow<PagingData<Story>>> =
        Result.Success(
            Pager(
                config = PagingConfig(
                    initialLoadSize = PaginationConst.NETWORK_PAGE_SIZE,
                    pageSize = PaginationConst.NETWORK_PAGE_SIZE,
                    enablePlaceholders = false
                ),
                remoteMediator = HomeRemoteMediator(database, service, token, location),
                pagingSourceFactory = {
                    database.homeDao().getAll()
                }
            ).flow.map {
                it.map { storyEntity ->
                    HomeResponseMapper.transform(storyEntity)
                }
            }
        )

    override suspend fun getStories(token: String): Result<HomeResponse> {
        return service.getStories(NetworkObject.wrapBearer(token)).awaitResponse()
    }
}
