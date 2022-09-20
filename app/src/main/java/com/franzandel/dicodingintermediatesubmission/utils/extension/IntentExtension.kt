package com.franzandel.dicodingintermediatesubmission.utils.extension

import android.content.Intent
import android.os.Build
import android.os.Parcelable
import java.io.Serializable

/**
 * Created by Franz Andel
 * on 20 September 2022.
 */

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Serializable?> Intent.getSerializable(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        getSerializableExtra(key, T::class.java)
    else
        @Suppress("DEPRECATION") getSerializableExtra(key) as T
}

inline fun <reified T : Parcelable> Intent.getParcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}
