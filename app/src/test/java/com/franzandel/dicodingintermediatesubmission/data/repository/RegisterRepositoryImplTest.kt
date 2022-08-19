package com.franzandel.dicodingintermediatesubmission.data.repository

import com.franzandel.dicodingintermediatesubmission.base.model.BaseResponse
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.data.model.RegisterRequest
import com.franzandel.dicodingintermediatesubmission.data.remote.RegisterRemoteSource
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
 * on 19 August 2022.
 */

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RegisterRepositoryImplTest {

    @Mock
    private lateinit var registerRemoteSource: RegisterRemoteSource
    private lateinit var registerRepository: RegisterRepository

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        registerRepository = RegisterRepositoryImpl(registerRemoteSource)
    }

    @Test
    fun `when Register return success`() = runTest {
        val fakeSuccessMessage = "register success"
        val expectedRegister = Result.Success(BaseResponse(message = fakeSuccessMessage))
        val registerRequest = RegisterRequest(
            email = "asdf@gmail.com",
            password = "asdfasdf",
            name = "asdf"
        )

        Mockito.`when`(registerRemoteSource.register(registerRequest)).thenReturn(expectedRegister)

        val actualRegister = registerRepository.register(registerRequest)
        Mockito.verify(registerRemoteSource).register(registerRequest)
        Assert.assertNotNull(actualRegister)
        Assert.assertTrue(actualRegister is Result.Success)
        Assert.assertEquals(fakeSuccessMessage, (actualRegister as Result.Success).data.message)
    }

    @Test
    fun `when Register return failed`() = runTest {
        val fakeFailedMessage = "register failed"
        val expectedRegister =
            Result.Error(400, BaseResponse(error = true, message = fakeFailedMessage))
        val registerRequest = RegisterRequest(
            email = "asdf@gmail.com",
            password = "asdfasdf",
            name = "asdf"
        )

        Mockito.`when`(registerRemoteSource.register(registerRequest)).thenReturn(expectedRegister)

        val actualRegister = registerRepository.register(registerRequest)
        Mockito.verify(registerRemoteSource).register(registerRequest)
        Assert.assertNotNull(actualRegister)
        Assert.assertTrue(actualRegister is Result.Error)
        Assert.assertEquals(fakeFailedMessage, (actualRegister as Result.Error).errorData?.message)
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

        Mockito.`when`(registerRemoteSource.register(registerRequest)).thenReturn(expectedRegister)

        val actualRegister = registerRepository.register(registerRequest)
        Mockito.verify(registerRemoteSource).register(registerRequest)
        Assert.assertNotNull(actualRegister)
        Assert.assertTrue(actualRegister is Result.Exception)
        Assert.assertEquals(
            fakeExceptionMessage,
            (actualRegister as Result.Exception).throwable.message
        )
    }
}

