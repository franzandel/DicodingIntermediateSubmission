package com.franzandel.dicodingintermediatesubmission.data.model.response

import androidx.annotation.Keep

@Keep
data class LoginResultResponse(
    val name: String,
    val token: String,
    val userId: String
)
