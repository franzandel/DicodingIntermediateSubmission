package com.franzandel.dicodingintermediatesubmission.domain.usecase

import com.franzandel.dicodingintermediatesubmission.base.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.base.usecase.BaseRequestUseCase
import com.franzandel.dicodingintermediatesubmission.data.Result
import com.franzandel.dicodingintermediatesubmission.domain.repository.HomeRepository

/**
 * Created by Franz Andel
 * on 10 May 2022.
 */

class ClearStorageUseCase(
    private val repository: HomeRepository,
    coroutineThread: CoroutineThread
) : BaseRequestUseCase<Result<Unit>, Unit>(coroutineThread) {

    override suspend fun getOperation(bodyRequest: Unit): Result<Unit> =
        repository.clearStorage()

}
