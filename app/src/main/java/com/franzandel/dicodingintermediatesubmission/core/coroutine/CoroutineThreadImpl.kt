package com.franzandel.dicodingintermediatesubmission.core.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class CoroutineThreadImpl @Inject constructor() : CoroutineThread {
    override val main: CoroutineDispatcher
        get() = Dispatchers.Main
    override val background: CoroutineDispatcher
        get() = Dispatchers.IO
    override val default: CoroutineDispatcher
        get() = Dispatchers.Default
}
