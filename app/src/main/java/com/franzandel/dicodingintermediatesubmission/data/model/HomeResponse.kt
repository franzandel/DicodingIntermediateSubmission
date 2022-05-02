package com.franzandel.dicodingintermediatesubmission.data.model

import androidx.annotation.Keep

@Keep
data class HomeResponse(
    val error: Boolean,
    val listStory: List<StoryResponse>,
    val message: String
)
