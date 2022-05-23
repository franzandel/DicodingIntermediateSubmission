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
        return suspendCancellableCoroutine { continuation ->
            service.getStories("Bearer $token").enqueue(object : Callback<HomeResponse> {
                override fun onResponse(
                    call: Call<HomeResponse>,
                    response: Response<HomeResponse>
                ) {
                    continuation.resumeWith(runCatching {
                        if (response.isSuccessful) {
                            val body = response.body()
                            if (body == null) {
                                Result.Exception(NullPointerException("Response body is null"))
                            } else {
                                Result.Success(body)
                            }
                        } else {
                            val loginResponse = Gson().fromJson<HomeResponse>(
                                response.errorBody()?.charStream()?.readText(),
                                object : TypeToken<HomeResponse>() {}.type
                            )
                            Result.Error(HttpException(response), loginResponse)
                        }
                    })
                }

                override fun onFailure(call: Call<HomeResponse>, t: Throwable) {
                    if (continuation.isCancelled) return
                    continuation.resume(Result.Exception(t))
                }
            })
        }
    }
}
