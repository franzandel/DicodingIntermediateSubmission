package com.franzandel.dicodingintermediatesubmission.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.franzandel.dicodingintermediatesubmission.data.model.StoryEntity

/**
 * Created by Franz Andel <franz.andel@ovo.id>
 * on 09 July 2022.
 */

@Dao
interface HomeDao {
    @Query("SELECT * FROM story")
    fun getAll(): PagingSource<Int, StoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(home: List<StoryEntity>)

    @Delete
    fun delete(home: StoryEntity)

    @Query("DELETE FROM story")
    fun deleteAll()
}

