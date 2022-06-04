package com.franzandel.dicodingintermediatesubmission.utils

import com.google.gson.Gson

/**
 * Created by Franz Andel
 * on 21 May 2022.
 */

object GsonUtils {

    val gson = Gson()

    fun toJsonString(dataclass: Any): String {
        return gson.toJson(dataclass)
    }

    fun <T> fromJsonString(jsonString: String, modelClass: Class<T>): T {
        return gson.fromJson(jsonString, modelClass)
    }
}
