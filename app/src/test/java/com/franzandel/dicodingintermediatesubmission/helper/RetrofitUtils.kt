package com.franzandel.dicodingintermediatesubmission.helper

import com.franzandel.dicodingintermediatesubmission.base.model.BaseResponse
import com.franzandel.dicodingintermediatesubmission.data.model.response.HomeResponse
import com.franzandel.dicodingintermediatesubmission.data.model.response.LoginResponse
import com.franzandel.dicodingintermediatesubmission.utils.GsonUtils
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringWriter

/**
 * Created by Franz Andel
 * on 02 August 2022.
 */

object RetrofitUtils {
    fun getLoginResponseFromJson(fileName: String): LoginResponse {
        val jsonString = getJsonString(fileName)
        return GsonUtils.fromJsonString(jsonString, LoginResponse::class.java)
    }

    fun getRegisterResponseFromJson(fileName: String): BaseResponse {
        val jsonString = getJsonString(fileName)
        return GsonUtils.fromJsonString(jsonString, BaseResponse::class.java)
    }

    fun getHomeResponseFromJson(fileName: String): HomeResponse {
        val jsonString = getJsonString(fileName)
        return GsonUtils.fromJsonString(jsonString, HomeResponse::class.java)
    }

    private fun getJsonString(filePath: String): String {
        val inputStream = javaClass.classLoader?.getResourceAsStream(filePath)

        val writer = StringWriter()
        val buffer = CharArray(1024)
        inputStream.use { stream ->
            val reader = BufferedReader(InputStreamReader(stream, "UTF-8"))
            var n: Int
            while (reader.read(buffer).also { n = it } != -1) {
                writer.write(buffer, 0, n)
            }
        }

        return writer.toString()
    }
}
