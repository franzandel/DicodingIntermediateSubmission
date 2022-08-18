package com.franzandel.dicodingintermediatesubmission.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.franzandel.dicodingintermediatesubmission.helper.MainDispatcherRule
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThreadImpl
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.data.model.LoginRequest
import com.franzandel.dicodingintermediatesubmission.domain.model.Login
import com.franzandel.dicodingintermediatesubmission.domain.usecase.LoginUseCase
import com.franzandel.dicodingintermediatesubmission.helper.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Franz Andel
 * on 01 August 2022.
 */

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @Mock
    private lateinit var loginUseCase: LoginUseCase
    private lateinit var loginViewModel: LoginViewModel
    private val coroutineThread: CoroutineThread = CoroutineThreadImpl()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(loginUseCase, coroutineThread)
    }

    @Test
    fun `when Login return success`() = runTest {
        val expectedLogin = Result.Success(Login(error = false, message = "login success"))
        val loginRequest = LoginRequest(
            email = "asdf@gmail.com",
            password = "asdfasdf"
        )

        `when`(loginUseCase(loginRequest)).thenReturn(expectedLogin)

        loginViewModel.login(loginRequest, isUsernameValid = true, isPasswordValid = true)
        val actualLogin = loginViewModel.loginResult.getOrAwaitValue()
        Mockito.verify(loginUseCase).invoke(loginRequest)
        Assert.assertNotNull(actualLogin)
        Assert.assertNotNull(actualLogin.success)
    }

    @Test
    fun `when Login return failed`() = runTest {
        val expectedLogin = Result.Error(400, Login(error = true, message = "login failed"))
        val loginRequest = LoginRequest(
            email = "asdf@gmail.com",
            password = "12345"
        )

        `when`(loginUseCase(loginRequest)).thenReturn(expectedLogin)

        loginViewModel.login(loginRequest, isUsernameValid = true, isPasswordValid = true)
        val actualLogin = loginViewModel.loginResult.getOrAwaitValue()
        Mockito.verify(loginUseCase).invoke(loginRequest)
        Assert.assertNotNull(actualLogin)
        Assert.assertNotNull(actualLogin.error)
        Assert.assertEquals(R.string.login_failed, actualLogin.error)
    }

    @Test
    fun `when Login return exception`() = runTest {
        val expectedLogin = Result.Exception(Exception("unexpected error"))
        val loginRequest = LoginRequest(
            email = "asdf@gmail.com",
            password = "12345"
        )

        `when`(loginUseCase(loginRequest)).thenReturn(expectedLogin)

        loginViewModel.login(loginRequest, isUsernameValid = true, isPasswordValid = true)
        val actualLogin = loginViewModel.loginResult.getOrAwaitValue()
        Mockito.verify(loginUseCase).invoke(loginRequest)
        Assert.assertNotNull(actualLogin)
        Assert.assertNotNull(actualLogin.error)
        Assert.assertEquals(R.string.system_error, actualLogin.error)
    }
}
