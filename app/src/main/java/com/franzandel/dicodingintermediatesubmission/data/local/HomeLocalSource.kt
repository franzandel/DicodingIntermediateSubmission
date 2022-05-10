package com.franzandel.dicodingintermediatesubmission.data.local

import com.franzandel.dicodingintermediatesubmission.data.Result

/**
 * Created by Franz Andel
 * on 10 May 2022.
 */

interface HomeLocalSource {
    suspend fun clearStorage(): Result<Unit>
}
