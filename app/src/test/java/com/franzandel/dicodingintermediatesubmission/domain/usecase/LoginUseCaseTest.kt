package com.franzandel.dicodingintermediatesubmission.domain.usecase

import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThreadImpl
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.data.model.LoginRequest
import com.franzandel.dicodingintermediatesubmission.domain.model.Login
import com.franzandel.dicodingintermediatesubmission.domain.repository.LoginRepository
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
 * on 02 August 2022.
 */

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginUseCaseTest {

    @Mock
    private lateinit var loginRepository: LoginRepository
    private lateinit var loginUseCase: LoginUseCase
    private val coroutineThread: CoroutineThread = CoroutineThreadImpl()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        loginUseCase = LoginUseCase(loginRepository, coroutineThread)
    }

    @Test
    fun `when Login return success`() = runTest {
        val expectedLogin = Result.Success(Login(error = false, message = "login success"))
        val loginRequest = LoginRequest(
            email = "asdf@gmail.com",
            password = "asdfasdf"
        )

        Mockito.`when`(loginRepository.login(loginRequest)).thenReturn(expectedLogin)

        val actualLogin = loginUseCase(loginRequest)
        Mockito.verify(loginRepository).login(loginRequest)
        Assert.assertNotNull(actualLogin)
        Assert.assertTrue(actualLogin is Result.Success)
    }

    @Test
    fun `when Login return failed`() = runTest {
        val fakeErrorMessage = "login failed"
        val expectedLogin = Result.Error(400, Login(error = true, message = fakeErrorMessage))
        val loginRequest = LoginRequest(
            email = "asdf@gmail.com",
            password = "12345"
        )

        Mockito.`when`(loginRepository.login(loginRequest)).thenReturn(expectedLogin)

        val actualLogin = loginUseCase(loginRequest)
        Mockito.verify(loginRepository).login(loginRequest)
        Assert.assertNotNull(actualLogin)
        Assert.assertTrue(actualLogin is Result.Error)
        Assert.assertEquals(fakeErrorMessage, (actualLogin as Result.Error).errorData?.message)
    }

    @Test
    fun `when Login return exception`() = runTest {
        val fakeExceptionMessage = "unexpected error"
        val expectedLogin = Result.Exception(Exception(fakeExceptionMessage))
        val loginRequest = LoginRequest(
            email = "asdf@gmail.com",
            password = "12345"
        )

        Mockito.`when`(loginRepository.login(loginRequest)).thenReturn(expectedLogin)

        val actualLogin = loginUseCase(loginRequest)
        Mockito.verify(loginRepository).login(loginRequest)
        Assert.assertNotNull(actualLogin)
        Assert.assertTrue(actualLogin is Result.Exception)
        Assert.assertEquals(fakeExceptionMessage, (actualLogin as Result.Exception).throwable.message)
    }
}
