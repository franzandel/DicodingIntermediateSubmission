package com.franzandel.dicodingintermediatesubmission.base.model

import androidx.annotation.Keep

/**
 * Created by Franz Andel
 * on 29 May 2022.
 */

@Keep
open class BaseResponse(
    val error: Boolean = false,
    val message: String = ""
)
