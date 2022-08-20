package com.franzandel.dicodingintermediatesubmission.domain.usecase

import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThreadImpl
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.domain.repository.HomeRepository
import com.franzandel.dicodingintermediatesubmission.helper.MainDispatcherRule
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
class GetLocationPreferenceUseCaseTest {

    @Mock
    private lateinit var homeRepository: HomeRepository

    private lateinit var getLocationPreferenceUseCase: GetLocationPreferenceUseCase
    private val coroutineThread: CoroutineThread = CoroutineThreadImpl()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        getLocationPreferenceUseCase = GetLocationPreferenceUseCase(homeRepository, coroutineThread)
    }

    @Test
    fun `when getLocation return success`() = runTest {
        val fakeLocation = 0
        val expectedGetLocation = Result.Success(flowOf(fakeLocation))

        Mockito.`when`(homeRepository.getLocationPreference()).thenReturn(expectedGetLocation)

        val actualLocation = getLocationPreferenceUseCase()
        Mockito.verify(homeRepository).getLocationPreference()
        Assert.assertNotNull(actualLocation)
        Assert.assertTrue(actualLocation is Result.Success)
        Assert.assertEquals((actualLocation as Result.Success).data.first(), fakeLocation)
    }

    @Test
    fun `when getLocation return failed`() = runTest {
        val fakeLocation = 0
        val fakeResponseCode = 400
        val expectedGetLocation = Result.Error(fakeResponseCode, flowOf(fakeLocation))

        Mockito.`when`(homeRepository.getLocationPreference()).thenReturn(expectedGetLocation)

        val actualLocation = getLocationPreferenceUseCase()
        Mockito.verify(homeRepository).getLocationPreference()
        Assert.assertNotNull(actualLocation)
        Assert.assertTrue(actualLocation is Result.Error)
        Assert.assertEquals((actualLocation as Result.Error).responseCode, fakeResponseCode)
    }

    @Test
    fun `when getLocation return exception`() = runTest {
        val fakeExceptionMessage = "location exception"
        val expectedGetLocation = Result.Exception(Exception(fakeExceptionMessage))

        Mockito.`when`(homeRepository.getLocationPreference()).thenReturn(expectedGetLocation)

        val actualLocation = getLocationPreferenceUseCase()
        Mockito.verify(homeRepository).getLocationPreference()
        Assert.assertNotNull(actualLocation)
        Assert.assertTrue(actualLocation is Result.Exception)
        Assert.assertEquals(
            (actualLocation as Result.Exception).throwable.message,
            fakeExceptionMessage
        )
    }
}
