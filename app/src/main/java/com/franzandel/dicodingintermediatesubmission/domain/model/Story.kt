package com.franzandel.dicodingintermediatesubmission.domain.model

/**
 * Created by Franz Andel
 * on 02 May 2022.
 */

data class Story(
    val createdAt: String,
    val description: String,
    val id: String,
    val latitude: Double?,
    val longitude: Double?,
    val name: String,
    val photoUrl: String
)
