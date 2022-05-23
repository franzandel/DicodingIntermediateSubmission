package com.franzandel.dicodingintermediatesubmission.domain.repository

import com.franzandel.dicodingintermediatesubmission.base.model.Result
import com.franzandel.dicodingintermediatesubmission.data.model.AddStoryRequest
import com.franzandel.dicodingintermediatesubmission.domain.model.AddStory

/**
 * Created by Franz Andel
 * on 07 May 2022.
 */

interface AddStoryRepository {
    suspend fun uploadImage(token: String, addStoryRequest: AddStoryRequest): Result<AddStory>
}
