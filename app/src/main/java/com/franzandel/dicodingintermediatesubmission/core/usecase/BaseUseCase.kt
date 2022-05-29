package com.franzandel.dicodingintermediatesubmission.core.usecase

import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import kotlinx.coroutines.withContext

/**
 * Created by Franz Andel
 * on 30 April 2022.
 */

abstract class BaseUseCase<Output>(
    private val coroutineThread: CoroutineThread
) {

    suspend operator fun invoke(): Output {
        return withContext(coroutineThread.background) {
            getOperation()
        }
    }

    protected abstract suspend fun getOperation(): Output
}
