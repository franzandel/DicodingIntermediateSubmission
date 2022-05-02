package com.franzandel.dicodingintermediatesubmission.data.remote

import androidx.paging.PagingData
import com.franzandel.dicodingintermediatesubmission.domain.model.Story
import kotlinx.coroutines.flow.Flow

/**
 * Created by Franz Andel
 * on 02 May 2022.
 */

interface HomeRemoteSource {
    suspend fun getStories(token: String): Flow<PagingData<Story>>
}
