package com.franzandel.dicodingintermediatesubmission.domain.usecase

import androidx.paging.PagingData
import com.franzandel.dicodingintermediatesubmission.base.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.base.usecase.BaseUseCase
import com.franzandel.dicodingintermediatesubmission.data.LoginRepository
import com.franzandel.dicodingintermediatesubmission.data.Result
import com.franzandel.dicodingintermediatesubmission.domain.model.Story
import com.franzandel.dicodingintermediatesubmission.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

/**
 * Created by Franz Andel
 * on 02 May 2022.
 */

class GetStoriesUseCase(
    private val homeRepository: HomeRepository,
    private val loginRepository: LoginRepository,
    coroutineThread: CoroutineThread
) : BaseUseCase<Result<Flow<PagingData<Story>>>>(coroutineThread) {

    override suspend fun getOperation(): Result<Flow<PagingData<Story>>> {
        return when (val result = loginRepository.getToken()) {
            is Result.Success -> {
                Result.Success(homeRepository.getStories(result.data.first()))
            }
            is Result.Error -> Result.Error(result.exception)
            is Result.Exception -> Result.Exception(result.throwable)
        }
    }
}
