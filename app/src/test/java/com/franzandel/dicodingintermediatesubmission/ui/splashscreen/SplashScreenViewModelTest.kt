package com.franzandel.dicodingintermediatesubmission.ui.splashscreen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThreadImpl
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.domain.usecase.GetTokenUseCase
import com.franzandel.dicodingintermediatesubmission.helper.MainDispatcherRule
import com.franzandel.dicodingintermediatesubmission.helper.getOrAwaitValue
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
 * on 28 August 2022.
 */

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SplashScreenViewModelTest {

    @Mock
    private lateinit var getTokenUseCase: GetTokenUseCase
    private lateinit var splashScreenViewModel: SplashScreenViewModel
    private val coroutineThread: CoroutineThread = CoroutineThreadImpl()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        splashScreenViewModel = SplashScreenViewModel(getTokenUseCase, coroutineThread)
    }

    @Test
    fun `when getToken return success`() = runTest {
        val fakeToken = "asdf"
        val expectedIsTokenEmpty = false
        val expectedToken = Result.Success(flowOf(fakeToken))

        Mockito.`when`(getTokenUseCase()).thenReturn(expectedToken)

        splashScreenViewModel.getToken()
        val actualIsTokenEmpty = splashScreenViewModel.isTokenEmpty.getOrAwaitValue()
        Mockito.verify(getTokenUseCase).invoke()
        Assert.assertNotNull(actualIsTokenEmpty)
        Assert.assertEquals(actualIsTokenEmpty, expectedIsTokenEmpty)
    }

    @Test
    fun `when getToken return failed`() = runTest {
        val fakeToken = "asdf"
        val expectedIsTokenEmpty = true
        val expectedToken = Result.Error(400, flowOf(fakeToken))

        Mockito.`when`(getTokenUseCase()).thenReturn(expectedToken)

        splashScreenViewModel.getToken()
        val actualIsTokenEmpty = splashScreenViewModel.isTokenEmpty.getOrAwaitValue()
        Mockito.verify(getTokenUseCase).invoke()
        Assert.assertNotNull(actualIsTokenEmpty)
        Assert.assertEquals(actualIsTokenEmpty, expectedIsTokenEmpty)
    }

    @Test
    fun `when getToken return exception`() = runTest {
        val expectedToken = Result.Exception(Exception("unexpected error"))
        val expectedIsTokenEmpty = true

        Mockito.`when`(getTokenUseCase()).thenReturn(expectedToken)

        splashScreenViewModel.getToken()
        val actualIsTokenEmpty = splashScreenViewModel.isTokenEmpty.getOrAwaitValue()
        Mockito.verify(getTokenUseCase).invoke()
        Assert.assertNotNull(actualIsTokenEmpty)
        Assert.assertEquals(actualIsTokenEmpty, expectedIsTokenEmpty)
    }
}
