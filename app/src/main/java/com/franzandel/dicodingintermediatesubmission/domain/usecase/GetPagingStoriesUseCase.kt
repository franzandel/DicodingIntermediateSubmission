package com.franzandel.dicodingintermediatesubmission.domain.usecase

import androidx.paging.PagingData
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.core.usecase.BaseRequestUseCase
import com.franzandel.dicodingintermediatesubmission.domain.model.Story
import com.franzandel.dicodingintermediatesubmission.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Created by Franz Andel
 * on 02 May 2022.
 */

class GetPagingStoriesUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
    private val getTokenUseCase: GetTokenUseCase,
    coroutineThread: CoroutineThread
) : BaseRequestUseCase<Result<Flow<PagingData<Story>>>, Int>(coroutineThread) {

    override suspend fun getOperation(bodyRequest: Int): Result<Flow<PagingData<Story>>> {
        return when (val tokenResult = getTokenUseCase()) {
            is Result.Success -> {
                when (val pagingStoriesResult =
                    homeRepository.getPagingStories(tokenResult.data.first(), bodyRequest)) {
                    is Result.Success -> pagingStoriesResult
                    is Result.Error -> pagingStoriesResult
                    is Result.Exception -> pagingStoriesResult
                }
            }
            is Result.Error -> Result.Error(tokenResult.responseCode)
            is Result.Exception -> tokenResult
        }
    }
}
