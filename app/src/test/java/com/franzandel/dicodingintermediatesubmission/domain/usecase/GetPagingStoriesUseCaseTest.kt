package com.franzandel.dicodingintermediatesubmission.domain.usecase

import androidx.paging.PagingData
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThreadImpl
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.domain.model.Story
import com.franzandel.dicodingintermediatesubmission.domain.repository.HomeRepository
import com.franzandel.dicodingintermediatesubmission.helper.MainDispatcherRule
import com.franzandel.dicodingintermediatesubmission.helper.collectDataForTest
import com.franzandel.dicodingintermediatesubmission.test.RoomUtils
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
class GetPagingStoriesUseCaseTest {

    @Mock
    private lateinit var homeRepository: HomeRepository

    @Mock
    private lateinit var getTokenUseCase: GetTokenUseCase

    private lateinit var getPagingStoriesUseCase: GetPagingStoriesUseCase
    private val coroutineThread: CoroutineThread = CoroutineThreadImpl()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        getPagingStoriesUseCase =
            GetPagingStoriesUseCase(homeRepository, getTokenUseCase, coroutineThread)
    }

    @Test
    fun `when getPagingStories return success`() = runTest {
        val fakeToken = Result.Success(flowOf("asdf"))
        val fakeLocation = 0
        val fakePagingStories = PagingData.from(RoomUtils.getStories())
        val expectedPagingStories = Result.Success(flowOf(fakePagingStories))

        Mockito.`when`(getTokenUseCase()).thenReturn(fakeToken)
        Mockito.`when`(homeRepository.getPagingStories(fakeToken.data.first(), fakeLocation))
            .thenReturn(expectedPagingStories)

        val actualPagingStories = getPagingStoriesUseCase(fakeLocation)
        Mockito.verify(homeRepository).getPagingStories(fakeToken.data.first(), fakeLocation)
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

        Mockito.`when`(getTokenUseCase()).thenReturn(fakeToken)
        Mockito.`when`(homeRepository.getPagingStories(fakeToken.data.first(), fakeLocation))
            .thenReturn(expectedPagingStories)

        val actualPagingStories = getPagingStoriesUseCase(fakeLocation)
        Mockito.verify(homeRepository).getPagingStories(fakeToken.data.first(), fakeLocation)
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

        Mockito.`when`(getTokenUseCase()).thenReturn(fakeToken)
        Mockito.`when`(homeRepository.getPagingStories(fakeToken.data.first(), fakeLocation))
            .thenReturn(expectedPagingStories)

        val actualPagingStories = getPagingStoriesUseCase(fakeLocation)
        Mockito.verify(homeRepository).getPagingStories(fakeToken.data.first(), fakeLocation)
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

        Mockito.`when`(getTokenUseCase()).thenReturn(fakeToken)
        Mockito.`when`(homeRepository.getPagingStories(fakeToken.data.first(), fakeLocation))
            .thenReturn(expectedPagingStories)

        val actualPagingStories = getPagingStoriesUseCase(fakeLocation)
        Mockito.verify(homeRepository).getPagingStories(fakeToken.data.first(), fakeLocation)
        Assert.assertNotNull(actualPagingStories)
        Assert.assertTrue(actualPagingStories is Result.Exception)
        Assert.assertEquals(
            (actualPagingStories as Result.Exception).throwable.message,
            fakeExceptionMessage
        )
    }

    @Test
    fun `when getPagingStories get token return failed`() = runTest {
        val fakeResponseCode = 400
        val fakeTokenErrorMessage = "token error"
        val fakeToken = Result.Error(fakeResponseCode, flowOf(fakeTokenErrorMessage))
        val fakeLocation = 0
        val fakePagingStories = PagingData.from(RoomUtils.getStories())
        val expectedPagingStories = Result.Success(flowOf(fakePagingStories))

        Mockito.`when`(getTokenUseCase()).thenReturn(fakeToken)
        Mockito.`when`(homeRepository.getPagingStories(fakeTokenErrorMessage, fakeLocation))
            .thenReturn(expectedPagingStories)

        val actualPagingStories = getPagingStoriesUseCase(fakeLocation)
        Assert.assertNotNull(actualPagingStories)
        Assert.assertTrue(actualPagingStories is Result.Error)
        Assert.assertEquals(
            (actualPagingStories as Result.Error).responseCode,
            fakeResponseCode
        )
    }

    @Test
    fun `when getPagingStories get token return exception`() = runTest {
        val fakeExceptionMessage = "token exception"
        val fakeToken = Result.Exception(Exception(fakeExceptionMessage))
        val fakeLocation = 0
        val fakePagingStories = PagingData.from(RoomUtils.getStories())
        val expectedPagingStories = Result.Success(flowOf(fakePagingStories))

        Mockito.`when`(getTokenUseCase()).thenReturn(fakeToken)
        Mockito.`when`(homeRepository.getPagingStories(fakeExceptionMessage, fakeLocation))
            .thenReturn(expectedPagingStories)

        val actualPagingStories = getPagingStoriesUseCase(fakeLocation)
        Assert.assertNotNull(actualPagingStories)
        Assert.assertTrue(actualPagingStories is Result.Exception)
        Assert.assertEquals(
            (actualPagingStories as Result.Exception).throwable.message,
            fakeExceptionMessage
        )
    }
}
