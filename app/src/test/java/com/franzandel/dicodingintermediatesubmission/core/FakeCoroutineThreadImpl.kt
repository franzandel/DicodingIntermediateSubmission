package com.franzandel.dicodingintermediatesubmission.core

import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by Franz Andel <franz.andel@ovo.id>
 * on 27 August 2022.
 */

class FakeCoroutineThreadImpl: CoroutineThread {
    override val main: CoroutineDispatcher
        get() = Dispatchers.Unconfined
    override val background: CoroutineDispatcher
        get() = Dispatchers.Unconfined
    override val default: CoroutineDispatcher
        get() = Dispatchers.Unconfined
}
