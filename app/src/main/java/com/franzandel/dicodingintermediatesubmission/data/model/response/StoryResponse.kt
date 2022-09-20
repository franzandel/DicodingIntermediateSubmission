package com.franzandel.dicodingintermediatesubmission.data.model.response

import androidx.annotation.Keep

@Keep
data class StoryResponse(
    val createdAt: String,
    val description: String,
    val id: String,
    val lat: Double? = null,
    val lon: Double? = null,
    val name: String,
    val photoUrl: String
)
