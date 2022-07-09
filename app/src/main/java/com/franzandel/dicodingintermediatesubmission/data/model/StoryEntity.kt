package com.franzandel.dicodingintermediatesubmission.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "story")
data class StoryEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "created_at")
    val createdAt: String,
    val description: String,
    val lat: Double? = null,
    val lon: Double? = null,
    val name: String,
    @ColumnInfo(name = "photo_url")
    val photoUrl: String
)
