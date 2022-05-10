package com.franzandel.dicodingintermediatesubmission.data.repository

import androidx.paging.PagingData
import com.franzandel.dicodingintermediatesubmission.data.Result
import com.franzandel.dicodingintermediatesubmission.data.local.HomeLocalSource
import com.franzandel.dicodingintermediatesubmission.data.remote.HomeRemoteSource
import com.franzandel.dicodingintermediatesubmission.domain.model.Story
import com.franzandel.dicodingintermediatesubmission.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow

class HomeRepositoryImpl(
    private val remoteSource: HomeRemoteSource,
    private val localSource: HomeLocalSource
) : HomeRepository {

    override suspend fun getStories(token: String): Result<Flow<PagingData<Story>>> =
        remoteSource.getStories(token)

    override suspend fun clearStorage(): Result<Unit> = localSource.clearStorage()
}
