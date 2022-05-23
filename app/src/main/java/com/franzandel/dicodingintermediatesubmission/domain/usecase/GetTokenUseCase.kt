package com.franzandel.dicodingintermediatesubmission.domain.usecase

import com.franzandel.dicodingintermediatesubmission.base.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.base.usecase.BaseUseCase
import com.franzandel.dicodingintermediatesubmission.data.repository.LoginRepositoryImpl
import com.franzandel.dicodingintermediatesubmission.base.model.Result
import com.franzandel.dicodingintermediatesubmission.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow

/**
 * Created by Franz Andel
 * on 30 April 2022.
 */

class GetTokenUseCase(
    private val repository: LoginRepository,
    coroutineThread: CoroutineThread
) : BaseUseCase<Result<Flow<String>>>(coroutineThread) {

    override suspend fun getOperation(): Result<Flow<String>> {
        return repository.getToken()
    }
}
