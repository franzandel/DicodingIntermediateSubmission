package com.franzandel.dicodingintermediatesubmission.data.remote

import androidx.paging.PagingData
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.data.model.HomeResponse
import com.franzandel.dicodingintermediatesubmission.domain.model.Story
import kotlinx.coroutines.flow.Flow

/**
 * Created by Franz Andel
 * on 02 May 2022.
 */

interface HomeRemoteSource {
    suspend fun getPagingStories(token: String, location: Int): Result<Flow<PagingData<Story>>>
    suspend fun getStories(token: String): Result<HomeResponse>
}
