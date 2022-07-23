package com.franzandel.dicodingintermediatesubmission.domain.repository

import androidx.paging.PagingData
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.domain.model.Story
import kotlinx.coroutines.flow.Flow

/**
 * Created by Franz Andel
 * on 02 May 2022.
 */

interface HomeRepository {
    suspend fun getPagingStories(token: String, location: Int): Result<Flow<PagingData<Story>>>
    suspend fun clearStorage(): Result<Unit>
    suspend fun getStories(token: String): Result<List<Story>>
    suspend fun saveLocationPreference(locationPreference: Int): Result<Unit>
    suspend fun getLocationPreference(): Result<Flow<Int>>
}
