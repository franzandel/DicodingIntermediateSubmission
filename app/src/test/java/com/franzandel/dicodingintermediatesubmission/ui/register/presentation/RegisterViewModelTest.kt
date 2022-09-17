package com.franzandel.dicodingintermediatesubmission.ui.register.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThreadImpl
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.data.model.RegisterRequest
import com.franzandel.dicodingintermediatesubmission.domain.model.Register
import com.franzandel.dicodingintermediatesubmission.domain.usecase.RegisterUseCase
import com.franzandel.dicodingintermediatesubmission.helper.MainDispatcherRule
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
 * on 16 August 2022.
 */

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {

    @Mock
    private lateinit var registerUseCase: RegisterUseCase
    private lateinit var registerViewModel: RegisterViewModel
    private val coroutineThread: CoroutineThread = CoroutineThreadImpl()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(registerUseCase, coroutineThread)
    }

    @Test
    fun `when Register return success`() = runTest {
        val expectedRegister = Result.Success(Register(error = false, message = "register success"))
        val registerRequest = RegisterRequest(
            email = "asdf@gmail.com",
            password = "asdfasdf",
            name = "asdf"
        )

        `when`(registerUseCase(registerRequest)).thenReturn(expectedRegister)

        registerViewModel.register(
            registerRequest,
            confirmationPassword = "asdfasdf"
        )
        val actualRegister = registerViewModel.registerResult.getOrAwaitValue()
        Mockito.verify(registerUseCase).invoke(registerRequest)
        Assert.assertNotNull(actualRegister)
        Assert.assertNotNull(actualRegister.success)
    }

    @Test
    fun `when Register return failed`() = runTest {
        val expectedRegister =
            Result.Error(400, Register(error = true, message = "register failed"))
        val registerRequest = RegisterRequest(
            email = "asdf@gmail.com",
            password = "asdfasdf",
            name = "asdf"
        )

        `when`(registerUseCase(registerRequest)).thenReturn(expectedRegister)

        registerViewModel.register(
            registerRequest,
            confirmationPassword = "asdfasdf"
        )
        val actualRegister = registerViewModel.registerResult.getOrAwaitValue()
        Mockito.verify(registerUseCase).invoke(registerRequest)
        Assert.assertNotNull(actualRegister)
        Assert.assertNotNull(actualRegister.error)
        Assert.assertEquals(R.string.register_failed, actualRegister.error)
    }

    @Test
    fun `when Register return email already exist`() = runTest {
        val expectedRegister =
            Result.Error(400, Register(error = true, message = "Email is already taken"))
        val registerRequest = RegisterRequest(
            email = "asdf@gmail.com",
            password = "asdfasdf",
            name = "asdf"
        )

        `when`(registerUseCase(registerRequest)).thenReturn(expectedRegister)

        registerViewModel.register(
            registerRequest,
            confirmationPassword = "asdfasdf"
        )
        val actualRegister = registerViewModel.registerResult.getOrAwaitValue()
        Mockito.verify(registerUseCase).invoke(registerRequest)
        Assert.assertNotNull(actualRegister)
        Assert.assertNotNull(actualRegister.error)
        Assert.assertEquals(R.string.register_already_exist, actualRegister.error)
    }


    @Test
    fun `when Register return exception`() = runTest {
        val expectedRegister = Result.Exception(Exception("unexpected error"))
        val registerRequest = RegisterRequest(
            email = "asdf@gmail.com",
            password = "asdfasdf",
            name = "asdf"
        )

        `when`(registerUseCase(registerRequest)).thenReturn(expectedRegister)

        registerViewModel.register(
            registerRequest,
            confirmationPassword = "asdfasdf"
        )
        val actualRegister = registerViewModel.registerResult.getOrAwaitValue()
        Mockito.verify(registerUseCase).invoke(registerRequest)
        Assert.assertNotNull(actualRegister)
        Assert.assertNotNull(actualRegister.error)
        Assert.assertEquals(R.string.system_error, actualRegister.error)
    }
}
