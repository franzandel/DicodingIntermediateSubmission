package com.franzandel.dicodingintermediatesubmission.data.local

import com.franzandel.dicodingintermediatesubmission.data.Result
import kotlinx.coroutines.flow.Flow

/**
 * Created by Franz Andel
 * on 29 April 2022.
 */

interface LoginLocalSource {
    suspend fun saveToken(token: String): Result<Unit>
    suspend fun getToken(): Result<Flow<String>>
}
