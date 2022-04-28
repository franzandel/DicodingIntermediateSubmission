package com.franzandel.dicodingintermediatesubmission.domain.model

data class Login(
    val error: Boolean,
    val loginResult: LoginResult? = null,
    val message: String
)
