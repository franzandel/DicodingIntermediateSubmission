package com.franzandel.dicodingintermediatesubmission.data.model.request

/**
 * Created by Franz Andel
 * on 28 April 2022.
 */

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)
