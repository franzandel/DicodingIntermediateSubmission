package com.franzandel.dicodingintermediatesubmission.domain.model

import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import com.franzandel.dicodingintermediatesubmission.ui.formatter.DateFormatter
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
        get() = DateFormatter.formatLongDate(createdAt)
}
