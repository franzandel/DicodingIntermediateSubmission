package com.franzandel.dicodingintermediatesubmission.domain.usecase

import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.core.usecase.BaseUseCase
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.domain.model.Story
import com.franzandel.dicodingintermediatesubmission.domain.repository.HomeRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Created by Franz Andel
 * on 14 May 2022.
 */

class GetStoriesUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
    private val getTokenUseCase: GetTokenUseCase,
    coroutineThread: CoroutineThread
) : BaseUseCase<Result<List<Story>>>(coroutineThread) {

    override suspend fun getOperation(): Result<List<Story>> {
        return when (val tokenResult = getTokenUseCase()) {
            is Result.Success -> {
                when (val storiesResult = homeRepository.getStories(tokenResult.data.first())) {
                    is Result.Success -> storiesResult
                    is Result.Error -> storiesResult
                    is Result.Exception -> storiesResult
                }
            }
            is Result.Error -> Result.Error()
            is Result.Exception -> Result.Exception(tokenResult.throwable)
        }
    }
}
