package com.franzandel.dicodingintermediatesubmission.domain.usecase

import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThreadImpl
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.data.model.request.LoginRequest
import com.franzandel.dicodingintermediatesubmission.data.model.request.RegisterRequest
import com.franzandel.dicodingintermediatesubmission.domain.model.Login
import com.franzandel.dicodingintermediatesubmission.domain.model.Register
import com.franzandel.dicodingintermediatesubmission.domain.repository.RegisterRepository
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
 * on 18 August 2022.
 */

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RegisterUseCaseTest {

    @Mock
    private lateinit var registerRepository: RegisterRepository

    @Mock
    private lateinit var loginUseCase: LoginUseCase
    private lateinit var registerUseCase: RegisterUseCase
    private val coroutineThread: CoroutineThread = CoroutineThreadImpl()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        registerUseCase = RegisterUseCase(registerRepository, loginUseCase, coroutineThread)
    }

    @Test
    fun `when Register return success`() = runTest {
        val expectedLogin = Result.Success(Login(error = false, message = "login success"))
        val expectedRegister = Result.Success(Register(error = false, message = "register success"))
        val registerRequest = RegisterRequest(
            email = "asdf@gmail.com",
            password = "asdfasdf",
            name = "asdf"
        )
        val loginRequest = LoginRequest(
            email = "asdf@gmail.com",
            password = "asdfasdf"
        )

        Mockito.`when`(registerRepository.register(registerRequest)).thenReturn(expectedRegister)
        Mockito.`when`(loginUseCase(loginRequest)).thenReturn(expectedLogin)

        val actualRegister = registerUseCase(registerRequest)
        Mockito.verify(registerRepository).register(registerRequest)
        Assert.assertNotNull(actualRegister)
        Assert.assertTrue(actualRegister is Result.Success)
    }

    @Test
    fun `when Register return failed`() = runTest {
        val fakeErrorMessage = "register failed"
        val expectedRegister = Result.Error(400, Register(error = true, message = fakeErrorMessage))
        val registerRequest = RegisterRequest(
            email = "asdf@gmail.com",
            password = "asdfasdf",
            name = "asdf"
        )

        Mockito.`when`(registerRepository.register(registerRequest)).thenReturn(expectedRegister)

        val actualRegister = registerUseCase(registerRequest)
        Mockito.verify(registerRepository).register(registerRequest)
        Assert.assertNotNull(actualRegister)
        Assert.assertTrue(actualRegister is Result.Error)
        Assert.assertEquals(fakeErrorMessage, (actualRegister as Result.Error).errorData?.message)
    }

    @Test
    fun `when Register return exception`() = runTest {
        val fakeExceptionMessage = "unexpected error"
        val expectedRegister = Result.Exception(Exception(fakeExceptionMessage))
        val registerRequest = RegisterRequest(
            email = "asdf@gmail.com",
            password = "asdfasdf",
            name = "asdf"
        )

        Mockito.`when`(registerRepository.register(registerRequest)).thenReturn(expectedRegister)

        val actualRegister = registerUseCase(registerRequest)
        Mockito.verify(registerRepository).register(registerRequest)
        Assert.assertNotNull(actualRegister)
        Assert.assertTrue(actualRegister is Result.Exception)
        Assert.assertEquals(
            fakeExceptionMessage,
            (actualRegister as Result.Exception).throwable.message
        )
    }
}
