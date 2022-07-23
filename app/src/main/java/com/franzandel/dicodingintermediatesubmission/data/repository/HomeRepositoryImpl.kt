package com.franzandel.dicodingintermediatesubmission.data.repository

import androidx.paging.PagingData
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.data.local.HomeLocalSource
import com.franzandel.dicodingintermediatesubmission.data.mapper.HomeResponseMapper
import com.franzandel.dicodingintermediatesubmission.data.remote.HomeRemoteSource
import com.franzandel.dicodingintermediatesubmission.domain.model.Story
import com.franzandel.dicodingintermediatesubmission.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val remoteSource: HomeRemoteSource,
    private val localSource: HomeLocalSource
) : HomeRepository {

    override suspend fun getPagingStories(token: String, location: Int): Result<Flow<PagingData<Story>>> =
        remoteSource.getPagingStories(token, location)

    override suspend fun clearStorage(): Result<Unit> = localSource.clearStorage()

    override suspend fun getStories(token: String): Result<List<Story>> {
        return when (val result = remoteSource.getStories(token)) {
            is Result.Success -> Result.Success(HomeResponseMapper.transform(result.data.listStory))
            is Result.Error ->
                Result.Error(
                    result.responseCode,
                    result.errorData?.let {
                        HomeResponseMapper.transform(it.listStory)
                    }
                )
            is Result.Exception -> result
        }
    }

    override suspend fun saveLocationPreference(locationPreference: Int): Result<Unit> =
        localSource.saveLocationPreference(locationPreference)

    override suspend fun getLocationPreference(): Result<Flow<Int>> =
        localSource.getLocationPreference()
}
