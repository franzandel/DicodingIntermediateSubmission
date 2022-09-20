package com.franzandel.dicodingintermediatesubmission.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Franz Andel
 * on 12 July 2022.
 */

@Entity(tableName = "remote_keys")
data class RemoteKeysEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "prev_key")
    val prevKey: Int?,
    @ColumnInfo(name = "next_key")
    val nextKey: Int?
)
