package com.franzandel.dicodingintermediatesubmission.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.franzandel.dicodingintermediatesubmission.data.dao.HomeDao
import com.franzandel.dicodingintermediatesubmission.data.model.HomeEntity
import com.franzandel.dicodingintermediatesubmission.data.model.StoryEntity

/**
 * Created by Franz Andel <franz.andel@ovo.id>
 * on 09 July 2022.
 */

@Database(entities = [HomeEntity::class, StoryEntity::class], version = 1, exportSchema = false)
abstract class StoriesDatabase : RoomDatabase() {
    abstract fun homeDao(): HomeDao
}

