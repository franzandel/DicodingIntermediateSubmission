package com.franzandel.dicodingintermediatesubmission.data.remote

import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.data.model.LoginRequest
import com.franzandel.dicodingintermediatesubmission.data.service.LoginService
import com.franzandel.dicodingintermediatesubmission.helper.MainDispatcherRule
import com.franzandel.dicodingintermediatesubmission.helper.RetrofitUtils
import com.franzandel.dicodingintermediatesubmission.helper.enqueueResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Franz Andel
 * on 02 August 2022.
 */

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginRemoteSourceTest {

    private lateinit var loginRemoteSource: LoginRemoteSource

    private val mockWebServer = MockWebServer()
    private val service = Retrofit.Builder()
        .baseUrl(mockWebServer.url(""))
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(LoginService::class.java)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        loginRemoteSource = LoginRemoteSourceImpl(service)
    }

    @Test
    fun `login response success`() {
        runBlocking {
            mockWebServer.enqueueResponse("login_response.json")
            val fakeMoviesRes = RetrofitUtils.getLoginResponseFromJson()
            val loginRequest = LoginRequest(
                email = "asdf@gmail.com",
                password = "asdfasdf"
            )

            val moviesRes = loginRemoteSource.login(loginRequest)
            Assert.assertNotNull(moviesRes)
            Assert.assertTrue(moviesRes is Result.Success)
            Assert.assertEquals(
                fakeMoviesRes.loginResult?.name,
                (moviesRes as Result.Success).data.loginResult?.name
            )
        }
    }

    @Test
    fun `login response failed`() {
        runBlocking {
            val fakeErrorCode = 404
            mockWebServer.enqueueResponse(
                fileName = "login_error_response.json",
                responseCode = fakeErrorCode
            )
            val loginRequest = LoginRequest(
                email = "asdf@gmail.com",
                password = "1234"
            )

            val moviesRes = loginRemoteSource.login(loginRequest)
            Assert.assertNotNull(moviesRes)
            Assert.assertTrue(moviesRes is Result.Error)
            Assert.assertEquals(fakeErrorCode, (moviesRes as Result.Error).responseCode)
        }
    }
}
