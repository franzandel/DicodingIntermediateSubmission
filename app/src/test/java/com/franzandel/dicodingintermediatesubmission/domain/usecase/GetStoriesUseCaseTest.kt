package com.franzandel.dicodingintermediatesubmission.domain.usecase

import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThreadImpl
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.domain.model.Story
import com.franzandel.dicodingintermediatesubmission.domain.repository.HomeRepository
import com.franzandel.dicodingintermediatesubmission.helper.MainDispatcherRule
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
 * on 21 August 2022.
 */

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetStoriesUseCaseTest {

    @Mock
    private lateinit var homeRepository: HomeRepository

    @Mock
    private lateinit var getTokenUseCase: GetTokenUseCase

    private lateinit var getStoriesUseCase: GetStoriesUseCase
    private val coroutineThread: CoroutineThread = CoroutineThreadImpl()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        getStoriesUseCase =
            GetStoriesUseCase(homeRepository, getTokenUseCase, coroutineThread)
    }

    @Test
    fun `when getStories return success`() = runTest {
        val fakeToken = Result.Success(flowOf("asdf"))
        val expectedStories = Result.Success(RoomUtils.getStories())

        Mockito.`when`(getTokenUseCase()).thenReturn(fakeToken)
        Mockito.`when`(homeRepository.getStories(fakeToken.data.first()))
            .thenReturn(expectedStories)

        val actualPagingStories = getStoriesUseCase()
        Mockito.verify(homeRepository).getStories(fakeToken.data.first())
        Assert.assertNotNull(actualPagingStories)
        Assert.assertTrue(actualPagingStories is Result.Success)
        Assert.assertEquals(
            (actualPagingStories as Result.Success).data.size,
            expectedStories.data.size
        )
    }

    @Test
    fun `when getStories return empty success`() = runTest {
        val fakeToken = Result.Success(flowOf("asdf"))
        val expectedStories = Result.Success(listOf<Story>())

        Mockito.`when`(getTokenUseCase()).thenReturn(fakeToken)
        Mockito.`when`(homeRepository.getStories(fakeToken.data.first()))
            .thenReturn(expectedStories)

        val actualPagingStories = getStoriesUseCase()
        Mockito.verify(homeRepository).getStories(fakeToken.data.first())
        Assert.assertNotNull(actualPagingStories)
        Assert.assertTrue(actualPagingStories is Result.Success)
        Assert.assertEquals(
            (actualPagingStories as Result.Success).data.size,
            expectedStories.data.size
        )
    }

    @Test
    fun `when getStories return failed`() = runTest {
        val fakeToken = Result.Success(flowOf("asdf"))
        val fakeResponseCode = 400
        val expectedStories = Result.Error(fakeResponseCode, listOf<Story>())

        Mockito.`when`(getTokenUseCase()).thenReturn(fakeToken)
        Mockito.`when`(homeRepository.getStories(fakeToken.data.first()))
            .thenReturn(expectedStories)

        val actualPagingStories = getStoriesUseCase()
        Mockito.verify(homeRepository).getStories(fakeToken.data.first())
        Assert.assertNotNull(actualPagingStories)
        Assert.assertTrue(actualPagingStories is Result.Error)
        Assert.assertEquals(
            (actualPagingStories as Result.Error).responseCode,
            expectedStories.responseCode
        )
    }

    @Test
    fun `when getStories return exception`() = runTest {
        val fakeToken = Result.Success(flowOf("asdf"))
        val fakeExceptionMessage = "getStories exception"
        val expectedStories = Result.Exception(Exception(fakeExceptionMessage))

        Mockito.`when`(getTokenUseCase()).thenReturn(fakeToken)
        Mockito.`when`(homeRepository.getStories(fakeToken.data.first()))
            .thenReturn(expectedStories)

        val actualPagingStories = getStoriesUseCase()
        Mockito.verify(homeRepository).getStories(fakeToken.data.first())
        Assert.assertNotNull(actualPagingStories)
        Assert.assertTrue(actualPagingStories is Result.Exception)
        Assert.assertEquals(
            (actualPagingStories as Result.Exception).throwable.message,
            fakeExceptionMessage
        )
    }

    @Test
    fun `when getStories get token return failed`() = runTest {
        val fakeResponseCode = 400
        val fakeTokenErrorMessage = "token error"
        val fakeToken = Result.Error(fakeResponseCode, flowOf(fakeTokenErrorMessage))
        val expectedStories = Result.Success(RoomUtils.getStories())

        Mockito.`when`(getTokenUseCase()).thenReturn(fakeToken)
        Mockito.`when`(homeRepository.getStories(fakeTokenErrorMessage))
            .thenReturn(expectedStories)

        val actualPagingStories = getStoriesUseCase()
        Assert.assertNotNull(actualPagingStories)
        Assert.assertTrue(actualPagingStories is Result.Error)
        Assert.assertEquals(
            (actualPagingStories as Result.Error).responseCode,
            fakeResponseCode
        )
    }

    @Test
    fun `when getStories get token return exception`() = runTest {
        val fakeExceptionMessage = "token exception"
        val fakeToken = Result.Exception(Exception(fakeExceptionMessage))
        val expectedStories = Result.Success(RoomUtils.getStories())

        Mockito.`when`(getTokenUseCase()).thenReturn(fakeToken)
        Mockito.`when`(homeRepository.getStories(fakeExceptionMessage))
            .thenReturn(expectedStories)

        val actualPagingStories = getStoriesUseCase()
        Assert.assertNotNull(actualPagingStories)
        Assert.assertTrue(actualPagingStories is Result.Exception)
        Assert.assertEquals(
            (actualPagingStories as Result.Exception).throwable.message,
            fakeExceptionMessage
        )
    }
}
