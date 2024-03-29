package com.franzandel.dicodingintermediatesubmission.data.repository

import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.data.mapper.AddStoryResponseMapper
import com.franzandel.dicodingintermediatesubmission.data.model.request.AddStoryRequest
import com.franzandel.dicodingintermediatesubmission.data.remote.AddStoryRemoteSource
import com.franzandel.dicodingintermediatesubmission.domain.model.AddStory
import com.franzandel.dicodingintermediatesubmission.domain.repository.AddStoryRepository
import javax.inject.Inject

class AddStoryRepositoryImpl @Inject constructor(private val remoteSource: AddStoryRemoteSource) : AddStoryRepository {

    override suspend fun uploadImage(
        token: String,
        addStoryRequest: AddStoryRequest
    ): Result<AddStory> {
        return when (val result = remoteSource.uploadImage(token, addStoryRequest)) {
            is Result.Success -> Result.Success(AddStoryResponseMapper.transform(result.data))
            is Result.Error ->
                Result.Error(
                    result.responseCode,
                    result.errorData?.let {
                        AddStoryResponseMapper.transform(it)
                    }
                )
            is Result.Exception -> result
        }
    }
}
