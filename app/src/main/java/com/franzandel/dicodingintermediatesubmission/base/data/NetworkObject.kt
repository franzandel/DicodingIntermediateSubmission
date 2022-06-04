package com.franzandel.dicodingintermediatesubmission.base.data

/**
 * Created by Franz Andel
 * on 04 June 2022.
 */

object NetworkObject {
    private const val BEARER = "Bearer"

    fun wrapBearer(token: String): String {
        return "$BEARER $token"
    }
}
