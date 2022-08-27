package com.franzandel.dicodingintermediatesubmission.data.remote

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.data.database.StoriesDatabase
import com.franzandel.dicodingintermediatesubmission.data.service.HomeService
import com.franzandel.dicodingintermediatesubmission.test.RoomUtils
import com.franzandel.dicodingintermediatesubmission.test.enqueueResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Franz Andel
 * on 23 August 2022.
 */

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class HomeRemoteSourceTest {

    private lateinit var storiesDatabase: StoriesDatabase
    private lateinit var homeRemoteSource: HomeRemoteSource

    private val mockWebServer = MockWebServer()
    private val service = Retrofit.Builder()
        .baseUrl(mockWebServer.url(""))
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(HomeService::class.java)

    @Before
    fun setUp() {
        storiesDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            StoriesDatabase::class.java
        ).build()
        homeRemoteSource = HomeRemoteSourceImpl(storiesDatabase, service)
    }

    @After
    fun tearDown() {
        storiesDatabase.close()
    }

    @Test
    fun getPagingStories_response_success() = runBlocking {
        mockWebServer.enqueueResponse("stories_response.json")
        val fakeToken = "asdf"
        val fakeLocation = 0
        val fakeStoryEntities = RoomUtils.getStoryEntities()

        storiesDatabase.homeDao().deleteAll()
        storiesDatabase.homeDao().insertAll(fakeStoryEntities)

        val resultPagingStories = homeRemoteSource.getPagingStories(fakeToken, fakeLocation)

        Assert.assertTrue(resultPagingStories is Result.Success)
    }
}
