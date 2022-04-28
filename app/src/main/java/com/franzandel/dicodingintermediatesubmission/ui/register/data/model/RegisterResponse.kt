package com.franzandel.dicodingintermediatesubmission.ui.register.data.model

import androidx.annotation.Keep

@Keep
data class RegisterResponse(
    val error: Boolean,
    val message: String
)
