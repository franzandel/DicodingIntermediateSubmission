package com.franzandel.dicodingintermediatesubmission.data.model

import androidx.annotation.Keep

@Keep
data class LoginResponse(
    val error: Boolean,
    val loginResult: LoginResultResponse? = null,
    val message: String
)
