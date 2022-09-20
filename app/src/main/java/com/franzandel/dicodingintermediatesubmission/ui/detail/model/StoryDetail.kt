package com.franzandel.dicodingintermediatesubmission.ui.detail.model

import android.os.Parcelable
import com.franzandel.dicodingintermediatesubmission.utils.formatter.DateFormatter
import kotlinx.parcelize.Parcelize

/**
 * Created by Franz Andel
 * on 04 May 2022.
 */

@Parcelize
data class StoryDetail(
    val createdAt: String,
    val description: String,
    val id: String,
    val latitude: Double?,
    val longitude: Double?,
    val name: String,
    val photoUrl: String
): Parcelable {
    val formattedCreatedAt: String
        get() = DateFormatter.formatLongDate(createdAt)
}
