//package com.franzandel.dicodingintermediatesubmission.utils
//
//import com.google.gson.reflect.TypeToken
//import com.franzandel.dicodingintermediatesubmission.data.Result
//import com.google.gson.Gson
//import retrofit2.HttpException
//
///**
// * Created by Franz Andel
// * on 28 April 2022.
// */
//
//const val DEFAULT_CODE = "0"
//const val SERVER_ERROR_MSG = "Terjadi Kesalahan"
//const val SERVER_ERROR_CODE = "500"
//
//inline fun <T> Result<T>.onSuccess(handler: (response: T) -> Unit): Result<T> {
//    (this as? Result.Success)?.also { handler(value) }
//    return this
//}
//
//inline fun <T> Result<T>.onException(handler: (throwable: Throwable) -> Unit): Result<T> {
//    (this as? Result.Exception)?.also { handler(throwable) }
//    return this
//}
//
///**
// * Used for catching failed response and parsing it when calling API. if error response not recognized
// * it will be returned as "500 : Terjadi Keslahan Server".
// * @param responseCode is Http Error Code
// * @param errorCode is Application Error Code
// * @param message is message from error body
// * @param rawData if available will be returned as JSON Object
// */
//inline fun <T> Result<T>.onFailed(handler: (responseCode: Int, errorCode: String, message: String, rawData: String) -> Unit): NetworkResponse<T> {
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
//
///**
// * Same as onFailed but if E is provided as type object for rawData then this function will be used,
// * usage example :
// *
// * onFailed { responseCode, errorCode, message, rawData : BaseResponse -> }
// *
// * from that example rawData will be returned as BaseResponse.
// */
//@JvmName("onFailedError")
//inline fun <T, reified E> Result<T>.onFailed(handler: (responseCode: Int, errorCode: String, message: String, rawData: E) -> Unit): NetworkResponse<T> {
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
//
//
//fun HttpException.extractError(): ErrorResponse {
//    val rawErrorMessage = response()?.errorBody()?.string().orEmpty()
//    return ErrorResponseParser.parseResponse(rawErrorMessage)
//        ?: ErrorResponse()
//}
