package com.franzandel.dicodingintermediatesubmission.ui.detail

import android.os.Parcelable
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
): Parcelable
