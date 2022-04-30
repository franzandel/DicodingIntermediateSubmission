package com.franzandel.dicodingintermediatesubmission.base.usecase

import com.franzandel.dicodingintermediatesubmission.base.coroutine.CoroutineThread
import kotlinx.coroutines.withContext

/**
 * Created by Franz Andel
 * on 30 April 2022.
 */

abstract class BaseUseCase<Output>(
    private val coroutineThread: CoroutineThread
) {

    suspend fun execute(): Output {
        return withContext(coroutineThread.background) {
            getOperation()
        }
    }

    protected abstract suspend fun getOperation(): Output
}
