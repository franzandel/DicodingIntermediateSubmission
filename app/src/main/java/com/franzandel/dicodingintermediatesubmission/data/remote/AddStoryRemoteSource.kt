package com.franzandel.dicodingintermediatesubmission.data.remote

import com.franzandel.dicodingintermediatesubmission.data.Result
import com.franzandel.dicodingintermediatesubmission.data.model.AddStoryRequest
import com.franzandel.dicodingintermediatesubmission.data.model.AddStoryResponse

/**
 * Created by Franz Andel
 * on 07 May 2022.
 */

interface AddStoryRemoteSource {
    suspend fun uploadImage(token: String, addStoryRequest: AddStoryRequest): Result<AddStoryResponse>
}