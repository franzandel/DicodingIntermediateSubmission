package com.franzandel.dicodingintermediatesubmission.data.remotemediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.franzandel.dicodingintermediatesubmission.data.database.StoriesDatabase
import com.franzandel.dicodingintermediatesubmission.data.model.StoryEntity
import com.franzandel.dicodingintermediatesubmission.data.service.HomeService
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
 * on 27 August 2022.
 */

@OptIn(ExperimentalPagingApi::class)
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class HomeRemoteMediatorTest {

    private lateinit var storiesDatabase: StoriesDatabase

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
    }

    @After
    fun tearDown() {
        storiesDatabase.close()
    }

    @Test
    fun getPagingStories_mediator_result_success() = runBlocking {
        mockWebServer.enqueueResponse("stories_response.json")
        val fakeToken = "asdf"
        val fakeLocation = 0
        val remoteMediator = HomeRemoteMediator(storiesDatabase, service, fakeToken, fakeLocation)
        val pagingState = PagingState<Int, StoryEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        Assert.assertTrue(result is RemoteMediator.MediatorResult.Success)
        Assert.assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun getEmptyPagingStories_mediator_result_success() = runBlocking {
        mockWebServer.enqueueResponse("stories_empty_response.json")
        val fakeToken = "asdf"
        val fakeLocation = 0
        val remoteMediator = HomeRemoteMediator(storiesDatabase, service, fakeToken, fakeLocation)
        val pagingState = PagingState<Int, StoryEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        Assert.assertTrue(result is RemoteMediator.MediatorResult.Success)
        Assert.assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun getPagingStories_mediator_result_error() = runBlocking {
        mockWebServer.enqueueResponse("stories_error_response.json", responseCode = 400)
        val fakeToken = "asdf"
        val fakeLocation = 0
        val remoteMediator = HomeRemoteMediator(storiesDatabase, service, fakeToken, fakeLocation)
        val pagingState = PagingState<Int, StoryEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        Assert.assertTrue(result is RemoteMediator.MediatorResult.Error)
    }
}
