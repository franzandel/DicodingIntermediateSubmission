package com.franzandel.dicodingintermediatesubmission.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.franzandel.dicodingintermediatesubmission.data.dao.HomeDao
import com.franzandel.dicodingintermediatesubmission.data.dao.RemoteKeysDao
import com.franzandel.dicodingintermediatesubmission.data.model.entity.RemoteKeysEntity
import com.franzandel.dicodingintermediatesubmission.data.model.entity.StoryEntity

/**
 * Created by Franz Andel
 * on 09 July 2022.
 */

@Database(
    entities = [StoryEntity::class, RemoteKeysEntity::class],
    version = 1,
    exportSchema = false
)
abstract class StoriesDatabase : RoomDatabase() {
    abstract fun homeDao(): HomeDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}

