package com.franzandel.dicodingintermediatesubmission.domain.usecase

import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThreadImpl
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.domain.repository.LoginRepository
import com.franzandel.dicodingintermediatesubmission.helper.MainDispatcherRule
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
 * on 20 September 2022.
 */

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetTokenUseCaseTest {

    @Mock
    private lateinit var loginRepository: LoginRepository

    private lateinit var getTokenUseCase: GetTokenUseCase
    private val coroutineThread: CoroutineThread = CoroutineThreadImpl()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        getTokenUseCase = GetTokenUseCase(loginRepository, coroutineThread)
    }

    @Test
    fun `when getToken return success`() = runTest {
        val fakeResponse = flowOf("asdf")
        val expectedGetToken = Result.Success(fakeResponse)

        Mockito.`when`(loginRepository.getToken()).thenReturn(expectedGetToken)

        val actualGetToken = getTokenUseCase()
        Mockito.verify(loginRepository).getToken()
        Assert.assertNotNull(actualGetToken)
        Assert.assertTrue(actualGetToken is Result.Success)
        Assert.assertEquals((actualGetToken as Result.Success).data, fakeResponse)
    }

    @Test
    fun `when getToken return failed`() = runTest {
        val fakeResponseCode = 400
        val fakeResponse = flowOf("asdf")
        val expectedGetToken = Result.Error(fakeResponseCode, fakeResponse)

        Mockito.`when`(loginRepository.getToken()).thenReturn(expectedGetToken)

        val actualGetToken = getTokenUseCase()
        Mockito.verify(loginRepository).getToken()
        Assert.assertNotNull(actualGetToken)
        Assert.assertTrue(actualGetToken is Result.Error)
        Assert.assertEquals((actualGetToken as Result.Error).responseCode, fakeResponseCode)
    }

    @Test
    fun `when getToken return exception`() = runTest {
        val fakeExceptionMessage = "getToken exception"
        val expectedGetToken = Result.Exception(Exception(fakeExceptionMessage))

        Mockito.`when`(loginRepository.getToken()).thenReturn(expectedGetToken)

        val actualGetToken = getTokenUseCase()
        Mockito.verify(loginRepository).getToken()
        Assert.assertNotNull(actualGetToken)
        Assert.assertTrue(actualGetToken is Result.Exception)
        Assert.assertEquals(
            (actualGetToken as Result.Exception).throwable.message,
            fakeExceptionMessage
        )
    }
}
