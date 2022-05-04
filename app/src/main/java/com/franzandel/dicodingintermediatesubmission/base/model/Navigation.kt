package com.franzandel.dicodingintermediatesubmission.base.model

import android.os.Bundle
import androidx.core.os.bundleOf

/**
 * Created by Franz Andel
 * on 04 May 2022.
 */

data class Navigation(
    val destination: Class<*>,
    val bundle: Bundle = bundleOf()
)
