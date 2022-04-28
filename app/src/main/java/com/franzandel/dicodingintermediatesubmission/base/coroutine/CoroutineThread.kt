package com.franzandel.dicodingintermediatesubmission.base.coroutine

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created by Franz Andel
 * on 28 April 2022.
 */

interface CoroutineThread {
    val main: CoroutineDispatcher
    val background: CoroutineDispatcher
    val default: CoroutineDispatcher
}
