package com.franzandel.dicodingintermediatesubmission.data.model.request

import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Created by Franz Andel
 * on 07 May 2022.
 */

data class AddStoryRequest(
    val file: MultipartBody.Part,
    val description: RequestBody,
    val latitude: RequestBody?,
    val longitude: RequestBody?
)
