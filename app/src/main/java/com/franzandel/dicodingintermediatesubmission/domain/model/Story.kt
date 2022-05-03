package com.franzandel.dicodingintermediatesubmission.domain.model

/**
 * Created by Franz Andel
 * on 02 May 2022.
 */

data class Story(
    val createdAt: String,
    val description: String,
    val id: String,
    val lat: Double,
    val lon: Double,
    val name: String,
    val photoUrl: String
)