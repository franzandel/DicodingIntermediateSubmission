package com.franzandel.dicodingintermediatesubmission.utils

import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume

/**
 * Created by Franz Andel
 * on 28 May 2022.
 */

suspend inline fun <reified T : Any> Call<T>.awaitResponse(): Result<T> {
    return suspendCancellableCoroutine { continuation ->
        enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                continuation.resumeWith(runCatching {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body == null) {
                            Result.Exception(NullPointerException("Response body is null"))
                        } else {
                            Result.Success(body)
                        }
                    } else {
                        val errorData: T = getParsedErrorModel(response)
                        Result.Error(response.code(), errorData)
                    }
                })
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                if (continuation.isCancelled) return
                continuation.resume(Result.Exception(t))
            }
        })

        continuation.invokeOnCancellation {
            try {
                cancel()
            } catch (ignored: Throwable) {
            }
        }
    }
}

inline fun <reified T> getParsedErrorModel(response: Response<T>): T {
    return GsonUtils.gson.fromJson(
        response.errorBody()?.charStream()?.readText(), object : TypeToken<T>() {}.type
    )
}

inline fun <T : Any> Result<T>.onSuccess(handler: (response: T) -> Unit): Result<T> {
    (this as? Result.Success)?.also { handler(data) }
    return this
}

inline fun <T : Any> Result<T>.onError(handler: (responseCode: Int, rawData: T?) -> Unit): Result<T> {
    (this as? Result.Error<T>)?.also { handler(responseCode, errorData) }
    return this
}

inline fun <T : Any> Result<T>.onException(handler: (throwable: Throwable) -> Unit): Result<T> {
    (this as? Result.Exception)?.also { handler(throwable) }
    return this
}

