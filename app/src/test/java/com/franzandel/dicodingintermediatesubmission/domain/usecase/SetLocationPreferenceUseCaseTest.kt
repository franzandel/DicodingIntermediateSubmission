package com.franzandel.dicodingintermediatesubmission.domain.usecase

import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThreadImpl
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.domain.repository.HomeRepository
import com.franzandel.dicodingintermediatesubmission.helper.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class SetLocationPreferenceUseCaseTest {

    @Mock
    private lateinit var homeRepository: HomeRepository

    private lateinit var setLocationPreferenceUseCase: SetLocationPreferenceUseCase
    private val coroutineThread: CoroutineThread = CoroutineThreadImpl()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        setLocationPreferenceUseCase = SetLocationPreferenceUseCase(homeRepository, coroutineThread)
    }

    @Test
    fun `when setLocation return success`() = runTest {
        val fakeLocation = 0
        val fakeResponse = Unit
        val expectedSetLocation = Result.Success(fakeResponse)

        Mockito.`when`(homeRepository.saveLocationPreference(fakeLocation))
            .thenReturn(expectedSetLocation)

        val actualLocation = setLocationPreferenceUseCase(fakeLocation)
        Mockito.verify(homeRepository).saveLocationPreference(fakeLocation)
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

        Mockito.`when`(homeRepository.saveLocationPreference(fakeLocation))
            .thenReturn(expectedSetLocation)

        val actualLocation = setLocationPreferenceUseCase(fakeLocation)
        Mockito.verify(homeRepository).saveLocationPreference(fakeLocation)
        Assert.assertNotNull(actualLocation)
        Assert.assertTrue(actualLocation is Result.Error)
        Assert.assertEquals((actualLocation as Result.Error).responseCode, fakeResponseCode)
    }

    @Test
    fun `when setLocation return exception`() = runTest {
        val fakeLocation = 0
        val fakeExceptionMessage = "location exception"
        val expectedSetLocation = Result.Exception(Exception(fakeExceptionMessage))

        Mockito.`when`(homeRepository.saveLocationPreference(fakeLocation))
            .thenReturn(expectedSetLocation)

        val actualLocation = setLocationPreferenceUseCase(fakeLocation)
        Mockito.verify(homeRepository).saveLocationPreference(fakeLocation)
        Assert.assertNotNull(actualLocation)
        Assert.assertTrue(actualLocation is Result.Exception)
        Assert.assertEquals(
            (actualLocation as Result.Exception).throwable.message,
            fakeExceptionMessage
        )
    }
}
