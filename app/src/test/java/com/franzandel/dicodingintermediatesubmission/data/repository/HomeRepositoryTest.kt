package com.franzandel.dicodingintermediatesubmission.data.repository

import androidx.paging.PagingData
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.data.local.HomeLocalSource
import com.franzandel.dicodingintermediatesubmission.data.remote.HomeRemoteSource
import com.franzandel.dicodingintermediatesubmission.domain.model.Story
import com.franzandel.dicodingintermediatesubmission.domain.repository.HomeRepository
import com.franzandel.dicodingintermediatesubmission.helper.MainDispatcherRule
import com.franzandel.dicodingintermediatesubmission.helper.RoomUtils
import com.franzandel.dicodingintermediatesubmission.helper.collectDataForTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Franz Andel
 * on 20 August 2022.
 */

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeRepositoryTest {

    @Mock
    private lateinit var homeLocalSource: HomeLocalSource

    @Mock
    private lateinit var homeRemoteSource: HomeRemoteSource
    private lateinit var homeRepository: HomeRepository

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        homeRepository = HomeRepositoryImpl(homeRemoteSource, homeLocalSource)
    }

    @Test
    fun `when getPagingStories return success`() = runTest {
        val fakeToken = Result.Success(flowOf("asdf"))
        val fakeLocation = 0
        val fakePagingStories = PagingData.from(RoomUtils.getStories())
        val expectedPagingStories = Result.Success(flowOf(fakePagingStories))

        Mockito.`when`(homeRemoteSource.getPagingStories(fakeToken.data.first(), fakeLocation))
            .thenReturn(expectedPagingStories)

        val actualPagingStories =
            homeRepository.getPagingStories(fakeToken.data.first(), fakeLocation)
        Mockito.verify(homeRemoteSource).getPagingStories(fakeToken.data.first(), fakeLocation)
        Assert.assertNotNull(actualPagingStories)
        Assert.assertTrue(actualPagingStories is Result.Success)
        Assert.assertEquals(
            (actualPagingStories as Result.Success).data.first()
                .collectDataForTest(mainDispatcherRule.testDispatcher).size,
            fakePagingStories.collectDataForTest(mainDispatcherRule.testDispatcher).size
        )
    }

    @Test
    fun `when getPagingStories return empty success`() = runTest {
        val fakeToken = Result.Success(flowOf("asdf"))
        val fakeLocation = 0
        val fakePagingStories = PagingData.empty<Story>()
        val expectedPagingStories = Result.Success(flowOf(fakePagingStories))

        Mockito.`when`(homeRemoteSource.getPagingStories(fakeToken.data.first(), fakeLocation))
            .thenReturn(expectedPagingStories)

        val actualPagingStories =
            homeRepository.getPagingStories(fakeToken.data.first(), fakeLocation)
        Mockito.verify(homeRemoteSource).getPagingStories(fakeToken.data.first(), fakeLocation)
        Assert.assertNotNull(actualPagingStories)
        Assert.assertTrue(actualPagingStories is Result.Success)
        Assert.assertEquals(
            (actualPagingStories as Result.Success).data.first()
                .collectDataForTest(mainDispatcherRule.testDispatcher).size,
            fakePagingStories.collectDataForTest(mainDispatcherRule.testDispatcher).size
        )
    }

    @Test
    fun `when getPagingStories return failed`() = runTest {
        val fakeToken = Result.Success(flowOf("asdf"))
        val fakeLocation = 0
        val fakeResponseCode = 400
        val fakePagingStories = PagingData.empty<Story>()
        val expectedPagingStories = Result.Error(fakeResponseCode, flowOf(fakePagingStories))

        Mockito.`when`(homeRemoteSource.getPagingStories(fakeToken.data.first(), fakeLocation))
            .thenReturn(expectedPagingStories)

        val actualPagingStories =
            homeRepository.getPagingStories(fakeToken.data.first(), fakeLocation)
        Mockito.verify(homeRemoteSource).getPagingStories(fakeToken.data.first(), fakeLocation)
        Assert.assertNotNull(actualPagingStories)
        Assert.assertTrue(actualPagingStories is Result.Error)
        Assert.assertEquals(
            (actualPagingStories as Result.Error).responseCode,
            expectedPagingStories.responseCode
        )
    }

    @Test
    fun `when getPagingStories return exception`() = runTest {
        val fakeToken = Result.Success(flowOf("asdf"))
        val fakeLocation = 0
        val fakeExceptionMessage = "getPagingStories exception"
        val expectedPagingStories = Result.Exception(Exception(fakeExceptionMessage))

        Mockito.`when`(homeRemoteSource.getPagingStories(fakeToken.data.first(), fakeLocation))
            .thenReturn(expectedPagingStories)

        val actualPagingStories =
            homeRepository.getPagingStories(fakeToken.data.first(), fakeLocation)
        Mockito.verify(homeRemoteSource).getPagingStories(fakeToken.data.first(), fakeLocation)
        Assert.assertNotNull(actualPagingStories)
        Assert.assertTrue(actualPagingStories is Result.Exception)
        Assert.assertEquals(
            (actualPagingStories as Result.Exception).throwable.message,
            fakeExceptionMessage
        )
    }

    @Test
    fun `when clearStorage return success`() = runTest {
        val fakeResponse = Unit
        val expectedClearStorage = Result.Success(fakeResponse)

        Mockito.`when`(homeLocalSource.clearStorage()).thenReturn(expectedClearStorage)

        val actualClearStorage = homeRepository.clearStorage()
        Mockito.verify(homeLocalSource).clearStorage()
        Assert.assertNotNull(actualClearStorage)
        Assert.assertTrue(actualClearStorage is Result.Success)
        Assert.assertEquals((actualClearStorage as Result.Success).data, fakeResponse)
    }

    @Test
    fun `when clearStorage return failed`() = runTest {
        val fakeResponseCode = 400
        val fakeResponse = Unit
        val expectedClearStorage = Result.Error(fakeResponseCode, fakeResponse)

        Mockito.`when`(homeLocalSource.clearStorage()).thenReturn(expectedClearStorage)

        val actualClearStorage = homeRepository.clearStorage()
        Mockito.verify(homeLocalSource).clearStorage()
        Assert.assertNotNull(actualClearStorage)
        Assert.assertTrue(actualClearStorage is Result.Error)
        Assert.assertEquals((actualClearStorage as Result.Error).responseCode, fakeResponseCode)
    }

    @Test
    fun `when clearStorage return exception`() = runTest {
        val fakeExceptionMessage = "clearStorage exception"
        val expectedClearStorage = Result.Exception(Exception(fakeExceptionMessage))

        Mockito.`when`(homeLocalSource.clearStorage()).thenReturn(expectedClearStorage)

        val actualClearStorage = homeRepository.clearStorage()
        Mockito.verify(homeLocalSource).clearStorage()
        Assert.assertNotNull(actualClearStorage)
        Assert.assertTrue(actualClearStorage is Result.Exception)
        Assert.assertEquals(
            (actualClearStorage as Result.Exception).throwable.message,
            fakeExceptionMessage
        )
    }

    @Test
    fun `when setLocation return success`() = runTest {
        val fakeLocation = 0
        val fakeResponse = Unit
        val expectedSetLocation = Result.Success(fakeResponse)

        Mockito.`when`(homeLocalSource.saveLocationPreference(fakeLocation))
            .thenReturn(expectedSetLocation)

        val actualLocation = homeRepository.saveLocationPreference(fakeLocation)
        Mockito.verify(homeLocalSource).saveLocationPreference(fakeLocation)
        Assert.assertNotNull(actualLocation)
        Assert.assertTrue(actualLocation is Result.Success)
        Assert.assertEquals((actualLocation as Result.Success).data, fakeResponse)
    }

    @Test
    fun `when setLocation return failed`() = runTest {
        val fakeLocation = 0
        val fakeResponseCode = 400
        val fakeResponse = Unit
        val expectedSetLocation = Result.Error(fakeResponseCode, fakeResponse)

        Mockito.`when`(homeLocalSource.saveLocationPreference(fakeLocation))
            .thenReturn(expectedSetLocation)

        val actualLocation = homeRepository.saveLocationPreference(fakeLocation)
        Mockito.verify(homeLocalSource).saveLocationPreference(fakeLocation)
        Assert.assertNotNull(actualLocation)
        Assert.assertTrue(actualLocation is Result.Error)
        Assert.assertEquals((actualLocation as Result.Error).responseCode, fakeResponseCode)
    }

    @Test
    fun `when setLocation return exception`() = runTest {
        val fakeLocation = 0
        val fakeExceptionMessage = "location exception"
        val expectedSetLocation = Result.Exception(Exception(fakeExceptionMessage))

        Mockito.`when`(homeLocalSource.saveLocationPreference(fakeLocation))
            .thenReturn(expectedSetLocation)

        val actualLocation = homeRepository.saveLocationPreference(fakeLocation)
        Mockito.verify(homeLocalSource).saveLocationPreference(fakeLocation)
        Assert.assertNotNull(actualLocation)
        Assert.assertTrue(actualLocation is Result.Exception)
        Assert.assertEquals(
            (actualLocation as Result.Exception).throwable.message,
            fakeExceptionMessage
        )
    }

    @Test
    fun `when getLocation return success`() = runTest {
        val fakeLocation = 0
        val expectedGetLocation = Result.Success(flowOf(fakeLocation))

        Mockito.`when`(homeLocalSource.getLocationPreference()).thenReturn(expectedGetLocation)

        val actualLocation = homeRepository.getLocationPreference()
        Mockito.verify(homeLocalSource).getLocationPreference()
        Assert.assertNotNull(actualLocation)
        Assert.assertTrue(actualLocation is Result.Success)
        Assert.assertEquals((actualLocation as Result.Success).data.first(), fakeLocation)
    }

    @Test
    fun `when getLocation return failed`() = runTest {
        val fakeLocation = 0
        val fakeResponseCode = 400
        val expectedGetLocation = Result.Error(fakeResponseCode, flowOf(fakeLocation))

        Mockito.`when`(homeLocalSource.getLocationPreference()).thenReturn(expectedGetLocation)

        val actualLocation = homeRepository.getLocationPreference()
        Mockito.verify(homeLocalSource).getLocationPreference()
        Assert.assertNotNull(actualLocation)
        Assert.assertTrue(actualLocation is Result.Error)
        Assert.assertEquals((actualLocation as Result.Error).responseCode, fakeResponseCode)
    }

    @Test
    fun `when getLocation return exception`() = runTest {
        val fakeExceptionMessage = "location exception"
        val expectedGetLocation = Result.Exception(Exception(fakeExceptionMessage))

        Mockito.`when`(homeLocalSource.getLocationPreference()).thenReturn(expectedGetLocation)

        val actualLocation = homeRepository.getLocationPreference()
        Mockito.verify(homeLocalSource).getLocationPreference()
        Assert.assertNotNull(actualLocation)
        Assert.assertTrue(actualLocation is Result.Exception)
        Assert.assertEquals(
            (actualLocation as Result.Exception).throwable.message,
            fakeExceptionMessage
        )
    }
}
