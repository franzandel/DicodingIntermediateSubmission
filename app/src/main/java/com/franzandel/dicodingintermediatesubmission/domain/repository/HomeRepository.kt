package com.franzandel.dicodingintermediatesubmission.domain.repository

import androidx.paging.PagingData
import com.franzandel.dicodingintermediatesubmission.data.Result
import com.franzandel.dicodingintermediatesubmission.domain.model.Story
import kotlinx.coroutines.flow.Flow

/**
 * Created by Franz Andel
 * on 02 May 2022.
 */

interface HomeRepository {
    suspend fun getStories(token: String): Result<Flow<PagingData<Story>>>
}
