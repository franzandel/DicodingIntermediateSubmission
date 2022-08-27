package com.franzandel.dicodingintermediatesubmission.test

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source

/**
 * Created by Franz Andel
 * on 02 August 2022.
 */

fun MockWebServer.enqueueResponse(
    fileName: String,
    headers: Map<String, String> = emptyMap(),
    responseCode: Int = 200
) {
    val inputStream =
        javaClass.classLoader?.getResourceAsStream(fileName)
    val source = inputStream?.source()?.buffer()
    val mockResponse = MockResponse()

    for ((key, value) in headers) {
        mockResponse.addHeader(key, value)
    }

    source?.let {
        this.enqueue(
            mockResponse.setResponseCode(responseCode).setBody(it.readString(Charsets.UTF_8))
        )
    }
}
