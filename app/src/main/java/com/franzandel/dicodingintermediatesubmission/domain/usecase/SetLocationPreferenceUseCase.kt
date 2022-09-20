package com.franzandel.dicodingintermediatesubmission.domain.usecase

import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.core.usecase.BaseRequestUseCase
import com.franzandel.dicodingintermediatesubmission.domain.repository.HomeRepository
import javax.inject.Inject

/**
 * Created by Franz Andel
 * on 23 July 2022.
 */

class SetLocationPreferenceUseCase @Inject constructor(
    private val repository: HomeRepository,
    coroutineThread: CoroutineThread
) : BaseRequestUseCase<Result<Unit>, Int>(coroutineThread) {

    override suspend fun getOperation(bodyRequest: Int): Result<Unit> =
        repository.saveLocationPreference(bodyRequest)
}
