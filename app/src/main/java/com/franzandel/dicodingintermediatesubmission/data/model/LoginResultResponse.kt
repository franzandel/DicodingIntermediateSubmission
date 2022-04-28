package com.franzandel.dicodingintermediatesubmission.data.model

import androidx.annotation.Keep

@Keep
data class LoginResultResponse(
    val name: String,
    val token: String,
    val userId: String
)
