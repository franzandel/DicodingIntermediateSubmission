package com.franzandel.dicodingintermediatesubmission.data

import retrofit2.HttpException

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error<out T : Any>(val exception: HttpException, val errorData: T) : Result<T>()
    data class Exception(val throwable: Throwable) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success -> "Success[data=$data]"
            is Error -> "Error[exception=$exception, data=$errorData]"
            is Exception -> "Exception[throwable=$throwable]"
        }
    }
}
