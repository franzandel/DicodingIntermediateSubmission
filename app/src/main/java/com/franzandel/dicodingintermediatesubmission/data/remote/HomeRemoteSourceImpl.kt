package com.franzandel.dicodingintermediatesubmission.data.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.franzandel.dicodingintermediatesubmission.data.consts.PaginationConst
import com.franzandel.dicodingintermediatesubmission.data.mapper.HomeResponseMapper
import com.franzandel.dicodingintermediatesubmission.data.paging.HomePagingSource
import com.franzandel.dicodingintermediatesubmission.data.service.HomeService
import com.franzandel.dicodingintermediatesubmission.domain.model.Story
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HomeRemoteSourceImpl(private val service: HomeService) : HomeRemoteSource {

    override suspend fun getStories(token: String): Flow<PagingData<Story>> =
        Pager(
            config = PagingConfig(
                initialLoadSize = PaginationConst.NETWORK_PAGE_SIZE,
                pageSize = PaginationConst.NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                HomePagingSource(service, token)
            }
        ).flow.map {
            it.map { storyResponse ->
                HomeResponseMapper.transform(storyResponse)
            }
        }
}
