package com.franzandel.dicodingintermediatesubmission.domain.usecase

import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.core.usecase.BaseUseCase
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Franz Andel
 * on 30 April 2022.
 */

class GetTokenUseCase @Inject constructor(
    private val repository: LoginRepository,
    coroutineThread: CoroutineThread
) : BaseUseCase<Result<Flow<String>>>(coroutineThread) {

    override suspend fun getOperation(): Result<Flow<String>> {
        return repository.getToken()
    }
}
