package com.franzandel.dicodingintermediatesubmission.base.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class CoroutineThreadImpl : CoroutineThread {
    override val main: CoroutineDispatcher
        get() = Dispatchers.Main
    override val background: CoroutineDispatcher
        get() = Dispatchers.IO
    override val default: CoroutineDispatcher
        get() = Dispatchers.Default
}
