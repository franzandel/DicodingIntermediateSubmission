package com.franzandel.dicodingintermediatesubmission.domain.model

data class LoginResult(
    val name: String,
    val token: String,
    val userId: String
)
