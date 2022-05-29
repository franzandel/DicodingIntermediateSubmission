package com.franzandel.dicodingintermediatesubmission.utils

import com.franzandel.dicodingintermediatesubmission.core.model.Result

/**
 * Created by Franz Andel
 * on 29 April 2022.
 */

suspend fun <T: Any> suspendTryCatch(
    codeBlock: suspend () -> Result<T>
): Result<T> = try {
    codeBlock.invoke()
} catch (e: Exception) {
    Result.Exception(e)
}
