package com.franzandel.dicodingintermediatesubmission.data.dao

import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.franzandel.dicodingintermediatesubmission.data.database.StoriesDatabase
import com.franzandel.dicodingintermediatesubmission.test.RoomUtils
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Franz Andel
 * on 27 August 2022.
 */

@RunWith(AndroidJUnit4::class)
@MediumTest
class HomeDaoTest {

    private lateinit var database: StoriesDatabase
    private lateinit var dao: HomeDao
    private val storyEntities = RoomUtils.getStoryEntities()

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            StoriesDatabase::class.java
        ).build()
        dao = database.homeDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun saveStoryEntities_success() {
        runBlocking {
            dao.insertAll(storyEntities)

            val expected = PagingSource.LoadResult.Page(
                data = storyEntities,
                prevKey = null,
                nextKey = null,
                itemsBefore = 0,
                itemsAfter = 0
            )
            val actual = dao.getAll().load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            )
            Assert.assertEquals(expected, actual)
        }
    }

    @Test
    fun deleteStoryEntities_success() {
        runBlocking {
            dao.insertAll(storyEntities)
            dao.deleteAll()

            val expected = PagingSource.LoadResult.Page(
                data = listOf(),
                prevKey = null,
                nextKey = null,
                itemsBefore = 0,
                itemsAfter = 0
            )
            val actual = dao.getAll().load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            )
            Assert.assertEquals(expected, actual)
        }
    }
}
