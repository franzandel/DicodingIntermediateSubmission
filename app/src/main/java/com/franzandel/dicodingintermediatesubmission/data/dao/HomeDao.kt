package com.franzandel.dicodingintermediatesubmission.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.franzandel.dicodingintermediatesubmission.data.model.HomeEntity

/**
 * Created by Franz Andel <franz.andel@ovo.id>
 * on 09 July 2022.
 */

@Dao
interface HomeDao {
    @Query("SELECT * FROM home")
    fun getAll(): PagingSource<Int, HomeEntity>
//    fun getAllQuote(): PagingSource<Int, QuoteResponseItem>

//    @Query("SELECT * FROM home WHERE uid IN (:userIds)")
//    fun loadAllByIds(userIds: IntArray): List<HomeEntity>

//    @Query("SELECT * FROM home WHERE first_name LIKE :first AND " +
//        "last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): HomeEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertAll(vararg users: HomeEntity)
    fun insertAll(home: HomeEntity)

    @Delete
    fun delete(home: HomeEntity)

    @Query("DELETE FROM home")
    fun deleteAll()
}

