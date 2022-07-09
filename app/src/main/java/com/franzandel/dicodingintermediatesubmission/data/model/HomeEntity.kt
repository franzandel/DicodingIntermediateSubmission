package com.franzandel.dicodingintermediatesubmission.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "home")
data class HomeEntity(
    @ColumnInfo(name = "list_story")
    val listStory: List<StoryEntity>
)
