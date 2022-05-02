package com.franzandel.dicodingintermediatesubmission.ui.home

import androidx.paging.PagingData
import com.franzandel.dicodingintermediatesubmission.domain.model.Story

/**
 * Created by Franz Andel
 * on 02 May 2022.
 */

data class HomeResult(
    val success: PagingData<Story>? = null,
    val error: Int? = null
)
