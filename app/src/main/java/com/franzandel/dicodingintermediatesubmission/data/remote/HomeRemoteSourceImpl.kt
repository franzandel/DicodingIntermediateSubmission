package com.franzandel.dicodingintermediatesubmission.data.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.franzandel.dicodingintermediatesubmission.base.model.Result
import com.franzandel.dicodingintermediatesubmission.data.consts.PaginationConst
import com.franzandel.dicodingintermediatesubmission.data.mapper.HomeResponseMapper
import com.franzandel.dicodingintermediatesubmission.data.model.HomeResponse
import com.franzandel.dicodingintermediatesubmission.data.paging.HomePagingSource
import com.franzandel.dicodingintermediatesubmission.data.service.HomeService
import com.franzandel.dicodingintermediatesubmission.domain.model.Story
import com.franzandel.dicodingintermediatesubmission.utils.awaitResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import kotlin.coroutines.resume

class HomeRemoteSourceImpl(private val service: HomeService) : HomeRemoteSource {

    override suspend fun getPagingStories(token: String): Result<Flow<PagingData<Story>>> =
        Result.Success(
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
        )

    override suspend fun getStories(token: String): Result<HomeResponse> {
        return service.getStories("Bearer $token").awaitResponse()
    }
}
