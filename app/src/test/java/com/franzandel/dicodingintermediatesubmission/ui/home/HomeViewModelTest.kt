package com.franzandel.dicodingintermediatesubmission.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThreadImpl
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.domain.model.Story
import com.franzandel.dicodingintermediatesubmission.domain.usecase.ClearStorageUseCase
import com.franzandel.dicodingintermediatesubmission.domain.usecase.GetLocationPreferenceUseCase
import com.franzandel.dicodingintermediatesubmission.domain.usecase.GetPagingStoriesUseCase
import com.franzandel.dicodingintermediatesubmission.domain.usecase.SetLocationPreferenceUseCase
import com.franzandel.dicodingintermediatesubmission.helper.MainDispatcherRule
import com.franzandel.dicodingintermediatesubmission.helper.collectDataForTest
import com.franzandel.dicodingintermediatesubmission.helper.getOrAwaitValue
import com.franzandel.dicodingintermediatesubmission.test.RoomUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
 * on 19 August 2022.
 */

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @Mock
    private lateinit var getPagingStoriesUseCase: GetPagingStoriesUseCase

    @Mock
    private lateinit var clearStorageUseCase: ClearStorageUseCase

    @Mock
    private lateinit var getLocationPreferenceUseCase: GetLocationPreferenceUseCase

    @Mock
    private lateinit var setLocationPreferenceUseCase: SetLocationPreferenceUseCase

    private lateinit var homeViewModel: HomeViewModel
    private val coroutineThread: CoroutineThread = CoroutineThreadImpl()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        homeViewModel = HomeViewModel(
            getPagingStoriesUseCase,
            clearStorageUseCase,
            getLocationPreferenceUseCase,
            setLocationPreferenceUseCase,
            coroutineThread
        )
    }

    @Test
    fun `when getStories return success`() = runTest {
        val fakeLocation = 0
        val fakePagingStories = PagingData.from(RoomUtils.getStories())
        val expectedHome = Result.Success(flowOf(fakePagingStories))

        Mockito.`when`(getPagingStoriesUseCase(fakeLocation)).thenReturn(expectedHome)

        homeViewModel.getStories(fakeLocation)
        val actualHome = homeViewModel.homeResult.getOrAwaitValue()
        Mockito.verify(getPagingStoriesUseCase).invoke(fakeLocation)
        Assert.assertNotNull(actualHome)
        Assert.assertNotNull(actualHome.success)
        Assert.assertEquals(
            actualHome.success?.collectDataForTest(mainDispatcherRule.testDispatcher)?.size,
            fakePagingStories.collectDataForTest(mainDispatcherRule.testDispatcher).size
        )
    }

    @Test
    fun `when getStories return empty success`() {
        runTest {
            val fakePagingStories = PagingData.empty<Story>()
            val fakeLocation = 0
            val expectedHome = Result.Success(flowOf(fakePagingStories))

            Mockito.`when`(getPagingStoriesUseCase(fakeLocation)).thenReturn(expectedHome)

            homeViewModel.getStories(fakeLocation)
            val actualHome = homeViewModel.homeResult.getOrAwaitValue()
            Mockito.verify(getPagingStoriesUseCase).invoke(fakeLocation)
            Assert.assertNotNull(actualHome)
            Assert.assertNotNull(actualHome.success)
            Assert.assertEquals(
                actualHome.success?.collectDataForTest(mainDispatcherRule.testDispatcher)?.size,
                fakePagingStories.collectDataForTest(mainDispatcherRule.testDispatcher).size
            )
        }
    }

    @Test
    fun `when getStories return failed`() {
        runTest {
            val fakePagingStories = PagingData.empty<Story>()
            val fakeLocation = 0
            val expectedHome = Result.Error(400, flowOf(fakePagingStories))

            Mockito.`when`(getPagingStoriesUseCase(fakeLocation)).thenReturn(expectedHome)

            homeViewModel.getStories(fakeLocation)
            val actualHome = homeViewModel.homeResult.getOrAwaitValue()
            Mockito.verify(getPagingStoriesUseCase).invoke(fakeLocation)
            Assert.assertNotNull(actualHome)
            Assert.assertNotNull(actualHome.error)
            Assert.assertEquals(R.string.system_error, actualHome.error)
        }
    }

    @Test
    fun `when getStories return exception`() {
        runTest {
            val fakeLocation = 0
            val expectedHome = Result.Exception(Exception("getStories exception"))

            Mockito.`when`(getPagingStoriesUseCase(fakeLocation)).thenReturn(expectedHome)

            homeViewModel.getStories(fakeLocation)
            val actualHome = homeViewModel.homeResult.getOrAwaitValue()
            Mockito.verify(getPagingStoriesUseCase).invoke(fakeLocation)
            Assert.assertNotNull(actualHome)
            Assert.assertNotNull(actualHome.error)
            Assert.assertEquals(R.string.system_error, actualHome.error)
        }
    }
}
