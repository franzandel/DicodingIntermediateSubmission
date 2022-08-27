package com.franzandel.dicodingintermediatesubmission.data.remote

import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.data.database.StoriesDatabase
import com.franzandel.dicodingintermediatesubmission.data.service.HomeService
import com.franzandel.dicodingintermediatesubmission.helper.MainDispatcherRule
import com.franzandel.dicodingintermediatesubmission.helper.RetrofitUtils
import com.franzandel.dicodingintermediatesubmission.test.enqueueResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Franz Andel
 * on 21 August 2022.
 */

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeRemoteSourceTest {

    @Mock
    private lateinit var storiesDatabase: StoriesDatabase

    private lateinit var homeRemoteSource: HomeRemoteSource

    private val mockWebServer = MockWebServer()
    private val service = Retrofit.Builder()
        .baseUrl(mockWebServer.url(""))
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(HomeService::class.java)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        homeRemoteSource = HomeRemoteSourceImpl(storiesDatabase, service)
    }

    @Test
    fun `getStories response success`() {
        runBlocking {
            val fileName = "stories_response.json"
            mockWebServer.enqueueResponse(fileName)
            val fakeStoriesRes = RetrofitUtils.getHomeResponseFromJson(fileName)
            val fakeToken = "asdf"

            val pagingStoriesRes = homeRemoteSource.getStories(fakeToken)
            Assert.assertNotNull(pagingStoriesRes)
            Assert.assertTrue(pagingStoriesRes is Result.Success)
            Assert.assertEquals(
                fakeStoriesRes.listStory.size,
                (pagingStoriesRes as Result.Success).data.listStory.size
            )
        }
    }

    @Test
    fun `getStories response error`() {
        runBlocking {
            val fakeErrorCode = 404
            val fakeToken = "asdf"
            mockWebServer.enqueueResponse(
                fileName = "stories_error_response.json",
                responseCode = fakeErrorCode
            )

            val storiesRes = homeRemoteSource.getStories(fakeToken)
            Assert.assertNotNull(storiesRes)
            Assert.assertTrue(storiesRes is Result.Error)
            Assert.assertEquals(fakeErrorCode, (storiesRes as Result.Error).responseCode)
        }
    }
}
