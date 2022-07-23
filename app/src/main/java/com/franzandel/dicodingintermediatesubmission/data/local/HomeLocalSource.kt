package com.franzandel.dicodingintermediatesubmission.data.local

import com.franzandel.dicodingintermediatesubmission.core.model.Result
import kotlinx.coroutines.flow.Flow

/**
 * Created by Franz Andel
 * on 10 May 2022.
 */

interface HomeLocalSource {
    suspend fun clearStorage(): Result<Unit>
    suspend fun saveLocationPreference(locationPreference: Int): Result<Unit>
    suspend fun getLocationPreference(): Result<Flow<Int>>
}
