package com.franzandel.dicodingintermediatesubmission.domain.model

import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import java.util.Locale

/**
 * Created by Franz Andel
 * on 02 May 2022.
 */

data class Story(
    val createdAt: String,
    val description: String,
    val id: String,
    val latitude: Double?,
    val longitude: Double?,
    val name: String,
    val photoUrl: String
) {
    val formattedCreatedAt: String
        get() {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SZ", Locale.getDefault())
            return DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT)
                .format(sdf.parse(createdAt))
        }
}
