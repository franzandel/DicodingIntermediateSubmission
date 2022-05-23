package com.franzandel.dicodingintermediatesubmission.domain.usecase

import com.franzandel.dicodingintermediatesubmission.base.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.base.usecase.BaseUseCase
import com.franzandel.dicodingintermediatesubmission.data.repository.LoginRepositoryImpl
import com.franzandel.dicodingintermediatesubmission.base.model.Result
import com.franzandel.dicodingintermediatesubmission.domain.model.Story
import com.franzandel.dicodingintermediatesubmission.domain.repository.HomeRepository
import com.franzandel.dicodingintermediatesubmission.domain.repository.LoginRepository
import kotlinx.coroutines.flow.first

/**
 * Created by Franz Andel
 * on 14 May 2022.
 */

class GetStoriesUseCase(
    private val homeRepository: HomeRepository,
    private val loginRepository: LoginRepository,
    coroutineThread: CoroutineThread
) : BaseUseCase<Result<List<Story>>>(coroutineThread) {

    override suspend fun getOperation(): Result<List<Story>> {
        return when (val result = loginRepository.getToken()) {
            is Result.Success -> {
                homeRepository.getStories(result.data.first())
            }
            is Result.Error -> Result.Error(result.exception)
            is Result.Exception -> Result.Exception(result.throwable)
        }
    }
}
