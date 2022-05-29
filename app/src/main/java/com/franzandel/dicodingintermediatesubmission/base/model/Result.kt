package com.franzandel.dicodingintermediatesubmission.base.model

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error<out T : Any>(val responseCode: Int = -1, val errorData: T? = null) :
        Result<T>()

    data class Exception(val throwable: Throwable) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success -> "Success[data=$data]"
            is Error -> "Error[code=$responseCode, data=$errorData]"
            is Exception -> "Exception[throwable=$throwable]"
        }
    }
}
