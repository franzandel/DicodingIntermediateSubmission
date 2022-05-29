package com.franzandel.dicodingintermediatesubmission.data.remote

import com.franzandel.dicodingintermediatesubmission.base.model.BaseResponse
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.data.model.AddStoryRequest

/**
 * Created by Franz Andel
 * on 07 May 2022.
 */

interface AddStoryRemoteSource {
    suspend fun uploadImage(token: String, addStoryRequest: AddStoryRequest): Result<BaseResponse>
}
