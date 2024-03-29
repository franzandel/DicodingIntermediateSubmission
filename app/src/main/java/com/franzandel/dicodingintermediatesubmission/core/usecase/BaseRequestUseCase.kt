package com.franzandel.dicodingintermediatesubmission.core.usecase

import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import kotlinx.coroutines.withContext

/**
 * Created by Franz Andel
 * on 28 April 2022.
 */

abstract class BaseRequestUseCase<Output, BodyRequest>(
    private val coroutineThread: CoroutineThread
) {

    suspend operator fun invoke(bodyRequest: BodyRequest): Output {
        return withContext(coroutineThread.background) {
            getOperation(bodyRequest)
        }
    }

    protected abstract suspend fun getOperation(bodyRequest: BodyRequest): Output
}
