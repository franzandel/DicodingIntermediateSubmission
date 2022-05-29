package com.franzandel.dicodingintermediatesubmission.domain.usecase

import androidx.paging.PagingData
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.core.usecase.BaseUseCase
import com.franzandel.dicodingintermediatesubmission.domain.model.Story
import com.franzandel.dicodingintermediatesubmission.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

/**
 * Created by Franz Andel
 * on 02 May 2022.
 */

class GetPagingStoriesUseCase(
    private val homeRepository: HomeRepository,
    private val getTokenUseCase: GetTokenUseCase,
    coroutineThread: CoroutineThread
) : BaseUseCase<Result<Flow<PagingData<Story>>>>(coroutineThread) {

    override suspend fun getOperation(): Result<Flow<PagingData<Story>>> {
        return when (val tokenResult = getTokenUseCase()) {
            is Result.Success -> {
                when (val pagingStoriesResult =
                    homeRepository.getPagingStories(tokenResult.data.first())) {
                    is Result.Success -> pagingStoriesResult
                    is Result.Error -> pagingStoriesResult
                    is Result.Exception -> pagingStoriesResult
                }
            }
            is Result.Error -> Result.Error()
            is Result.Exception -> tokenResult
        }
    }
}
