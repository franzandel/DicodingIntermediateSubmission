package com.franzandel.dicodingintermediatesubmission.data.model

import androidx.annotation.Keep

@Keep
data class AddStoryResponse(
    val error: Boolean,
    val message: String
)
