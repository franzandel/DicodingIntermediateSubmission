package com.franzandel.dicodingintermediatesubmission.domain.usecase

import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.core.usecase.BaseRequestUseCase
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.domain.repository.HomeRepository
import javax.inject.Inject

/**
 * Created by Franz Andel
 * on 10 May 2022.
 */

class ClearStorageUseCase @Inject constructor(
    private val repository: HomeRepository,
    coroutineThread: CoroutineThread
) : BaseRequestUseCase<Result<Unit>, Unit>(coroutineThread) {

    override suspend fun getOperation(bodyRequest: Unit): Result<Unit> =
        repository.clearStorage()

}
