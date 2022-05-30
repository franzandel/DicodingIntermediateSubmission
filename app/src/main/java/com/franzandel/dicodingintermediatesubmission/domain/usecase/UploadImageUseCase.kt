package com.franzandel.dicodingintermediatesubmission.domain.usecase

import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.core.usecase.BaseRequestUseCase
import com.franzandel.dicodingintermediatesubmission.data.model.AddStoryRequest
import com.franzandel.dicodingintermediatesubmission.domain.model.AddStory
import com.franzandel.dicodingintermediatesubmission.domain.repository.AddStoryRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Created by Franz Andel
 * on 07 May 2022.
 */

class UploadImageUseCase @Inject constructor(
    private val repository: AddStoryRepository,
    private val getTokenUseCase: GetTokenUseCase,
    coroutineThread: CoroutineThread
) : BaseRequestUseCase<Result<AddStory>, AddStoryRequest>(coroutineThread) {

    override suspend fun getOperation(bodyRequest: AddStoryRequest): Result<AddStory> {
        return when (val tokenResult = getTokenUseCase()) {
            is Result.Success -> {
                when (val uploadImageResult = repository.uploadImage(tokenResult.data.first(), bodyRequest)) {
                    is Result.Success -> uploadImageResult
                    is Result.Error -> uploadImageResult
                    is Result.Exception -> uploadImageResult
                }
            }
            is Result.Error -> Result.Error()
            is Result.Exception -> tokenResult
        }
    }
}
