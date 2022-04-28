package com.franzandel.dicodingintermediatesubmission.ui.register.presentation

/**
 * Authentication result : success (user details) or error message.
 */
data class RegisterResult(
    val success: RegisterInUserView? = null,
    val error: Int? = null
)
