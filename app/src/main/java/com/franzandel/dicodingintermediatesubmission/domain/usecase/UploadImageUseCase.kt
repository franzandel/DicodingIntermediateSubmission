package com.franzandel.dicodingintermediatesubmission.domain.usecase

import com.franzandel.dicodingintermediatesubmission.base.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.base.usecase.BaseRequestUseCase
import com.franzandel.dicodingintermediatesubmission.data.Result
import com.franzandel.dicodingintermediatesubmission.data.model.AddStoryRequest
import com.franzandel.dicodingintermediatesubmission.domain.model.AddStory
import com.franzandel.dicodingintermediatesubmission.domain.repository.AddStoryRepository
import kotlinx.coroutines.flow.first

/**
 * Created by Franz Andel
 * on 07 May 2022.
 */

class UploadImageUseCase(
    private val repository: AddStoryRepository,
    private val getTokenUseCase: GetTokenUseCase,
    coroutineThread: CoroutineThread
) : BaseRequestUseCase<Result<AddStory>, AddStoryRequest>(coroutineThread) {

    override suspend fun getOperation(bodyRequest: AddStoryRequest): Result<AddStory> {
        return when (val result = getTokenUseCase.execute()) {
            is Result.Success -> {
                repository.uploadImage(result.data.first(), bodyRequest)
            }
            is Result.Error -> Result.Error(result.exception)
            is Result.Exception -> Result.Exception(result.throwable)
        }
    }
}
