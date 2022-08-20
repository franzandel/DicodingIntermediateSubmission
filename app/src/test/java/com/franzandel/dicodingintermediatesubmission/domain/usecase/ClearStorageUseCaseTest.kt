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
class ClearStorageUseCaseTest {

    @Mock
    private lateinit var homeRepository: HomeRepository

    private lateinit var clearStorageUseCase: ClearStorageUseCase
    private val coroutineThread: CoroutineThread = CoroutineThreadImpl()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        clearStorageUseCase = ClearStorageUseCase(homeRepository, coroutineThread)
    }

    @Test
    fun `when clearStorage return success`() = runTest {
        val fakeResponse = Unit
        val expectedClearStorage = Result.Success(fakeResponse)

        Mockito.`when`(homeRepository.clearStorage()).thenReturn(expectedClearStorage)

        val actualClearStorage = clearStorageUseCase()
        Mockito.verify(homeRepository).clearStorage()
        Assert.assertNotNull(actualClearStorage)
        Assert.assertTrue(actualClearStorage is Result.Success)
        Assert.assertEquals((actualClearStorage as Result.Success).data, fakeResponse)
    }

    @Test
    fun `when clearStorage return failed`() = runTest {
        val fakeResponseCode = 400
        val fakeResponse = Unit
        val expectedClearStorage = Result.Error(fakeResponseCode, fakeResponse)

        Mockito.`when`(homeRepository.clearStorage()).thenReturn(expectedClearStorage)

        val actualClearStorage = clearStorageUseCase()
        Mockito.verify(homeRepository).clearStorage()
        Assert.assertNotNull(actualClearStorage)
        Assert.assertTrue(actualClearStorage is Result.Error)
        Assert.assertEquals((actualClearStorage as Result.Error).responseCode, fakeResponseCode)
    }

    @Test
    fun `when clearStorage return exception`() = runTest {
        val fakeExceptionMessage = "clearStorage exception"
        val expectedClearStorage = Result.Exception(Exception(fakeExceptionMessage))

        Mockito.`when`(homeRepository.clearStorage()).thenReturn(expectedClearStorage)

        val actualClearStorage = clearStorageUseCase()
        Mockito.verify(homeRepository).clearStorage()
        Assert.assertNotNull(actualClearStorage)
        Assert.assertTrue(actualClearStorage is Result.Exception)
        Assert.assertEquals(
            (actualClearStorage as Result.Exception).throwable.message,
            fakeExceptionMessage
        )
    }
}
