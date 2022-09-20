package com.franzandel.dicodingintermediatesubmission.data.repository

import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.data.local.LoginLocalSource
import com.franzandel.dicodingintermediatesubmission.data.model.request.LoginRequest
import com.franzandel.dicodingintermediatesubmission.data.model.response.LoginResponse
import com.franzandel.dicodingintermediatesubmission.data.model.response.LoginResultResponse
import com.franzandel.dicodingintermediatesubmission.data.remote.LoginRemoteSource
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
class LoginRepositoryTest {

    @Mock
    private lateinit var loginLocalSource: LoginLocalSource

    @Mock
    private lateinit var loginRemoteSource: LoginRemoteSource
    private lateinit var loginRepository: LoginRepository

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        loginRepository = LoginRepositoryImpl(loginRemoteSource, loginLocalSource)
    }

    @Test
    fun `when Login return success`() = runTest {
        val expectedLogin = Result.Success(
            LoginResponse(
                LoginResultResponse(
                    name = "asdf",
                    token = "fakeToken",
                    userId = "112233"
                )
            )
        )
        val loginRequest = LoginRequest(
            email = "asdf@gmail.com",
            password = "asdfasdf"
        )

        Mockito.`when`(loginRemoteSource.login(loginRequest)).thenReturn(expectedLogin)

        val actualLogin = loginRepository.login(loginRequest)
        Mockito.verify(loginRemoteSource).login(loginRequest)
        Assert.assertNotNull(actualLogin)
        Assert.assertTrue(actualLogin is Result.Success)
    }

    @Test
    fun `when Login return failed`() = runTest {
        val expectedLogin = Result.Error(
            400,
            LoginResponse(
                LoginResultResponse(
                    name = "asdf",
                    token = "fakeToken",
                    userId = "112233"
                )
            )
        )
        val loginRequest = LoginRequest(
            email = "asdf@gmail.com",
            password = "12345"
        )

        Mockito.`when`(loginRemoteSource.login(loginRequest)).thenReturn(expectedLogin)

        val actualLogin = loginRepository.login(loginRequest)
        Mockito.verify(loginRemoteSource).login(loginRequest)
        Assert.assertNotNull(actualLogin)
        Assert.assertTrue(actualLogin is Result.Error)
    }

    @Test
    fun `when Login return exception`() = runTest {
        val expectedLogin = Result.Exception(Exception("unexpected error"))
        val loginRequest = LoginRequest(
            email = "asdf@gmail.com",
            password = "12345"
        )

        Mockito.`when`(loginRemoteSource.login(loginRequest)).thenReturn(expectedLogin)

        val actualLogin = loginRepository.login(loginRequest)
        Mockito.verify(loginRemoteSource).login(loginRequest)
        Assert.assertNotNull(actualLogin)
        Assert.assertTrue(actualLogin is Result.Exception)
    }
}
