package com.franzandel.dicodingintermediatesubmission.utils

import android.util.Log
import com.franzandel.dicodingintermediatesubmission.base.model.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.await
import kotlin.Exception
import kotlin.coroutines.resume

/**
 * Created by Franz Andel
 * on 28 May 2022.
 */

const val DEFAULT_CODE = "0"
const val SERVER_ERROR_MSG = "Terjadi Kesalahan"
const val SERVER_ERROR_CODE = "500"

suspend fun <T : Any> Call<T>.awaitResponse(): Result<T> {
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
                        Result.Exception(HttpException(response))
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

inline fun <T: Any> Result<T>.onSuccess(handler: (response: T) -> Unit): Result<T> {
    (this as? Result.Success)?.also { handler(data) }
    return this
}

inline fun <T: Any> Result<T>.onException(handler: (throwable: Throwable) -> Unit): Result<T> {
    (this as? Result.Exception)?.also { handler(throwable) }
    return this
}

/**
 * Used for catching failed response and parsing it when calling API. if error response not recognized
 * it will be returned as "500 : Terjadi Keslahan Server".
 * @param responseCode is Http Error Code
 * @param errorCode is Application Error Code
 * @param message is message from error body
 * @param rawData if available will be returned as JSON Object
 */
//inline fun <T: Any> Result<T>.onFailed(handler: (responseCode: Int, errorCode: String, message: String, rawData: String) -> Unit): Result<T> {
//    (this as? Result.Error)?.also {
//        val errorResponse = exception.extractError()
//        val errorCode = errorResponse.code.takeIf { it != DEFAULT_CODE } ?: SERVER_ERROR_CODE
//        val errorMessage = errorResponse.message?.takeIf { it.isNotBlank() }
//            ?: SERVER_ERROR_MSG
//        val rawData = errorResponse.data.orEmpty()
//
//        handler(exception.code(), errorCode, errorMessage, rawData)
//    }
//
//    return this
//}

/**
 * Same as onFailed but if E is provided as type object for rawData then this function will be used,
 * usage example :
 *
 * onFailed { responseCode, errorCode, message, rawData : BaseResponse -> }
 *
 * from that example rawData will be returned as BaseResponse.
 */
//@JvmName("onFailedError")
//inline fun <T: Any, reified E> Result<T>.onFailed(handler: (responseCode: Int, errorCode: String, message: String, rawData: E) -> Unit): Result<T> {
//    (this as? Result.Error)?.also {
//        val errorResponse = exception.extractError()
//        val errorCode = errorResponse.code.takeIf { it != DEFAULT_CODE } ?: SERVER_ERROR_CODE
//        val errorMessage = errorResponse.message?.takeIf { it.isNotBlank() }
//            ?: SERVER_ERROR_MSG
//
//        try {
//            val rawData: E = Gson().fromJson(
//                errorResponse.data.orEmpty(), object : TypeToken<E>() {}.type
//            )
//            handler(exception.code(), errorCode, errorMessage, rawData)
//        } catch (e: Exception) {
//            return Result.Exception(e)
//        }
//    }
//
//    return this
//}


//fun HttpException.extractError(): ErrorResponse {
//    val rawErrorMessage = response()?.errorBody()?.string().orEmpty()
//    return ErrorResponseParser.parseResponse(rawErrorMessage)
//        ?: ErrorResponse()
//}

suspend fun <T : Any> Call<T>.awaitOrNull(): T? {
    return try {
        this.await()
    } catch (e: Exception) {
        Log.d("Exception", e.message.orEmpty())
        null
    }
}
