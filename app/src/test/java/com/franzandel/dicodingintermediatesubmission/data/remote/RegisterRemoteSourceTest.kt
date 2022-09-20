package com.franzandel.dicodingintermediatesubmission.data.remote

import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.data.model.request.RegisterRequest
import com.franzandel.dicodingintermediatesubmission.data.service.RegisterService
import com.franzandel.dicodingintermediatesubmission.helper.MainDispatcherRule
import com.franzandel.dicodingintermediatesubmission.helper.RetrofitUtils
import com.franzandel.dicodingintermediatesubmission.test.enqueueResponse
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
 * on 19 August 2022.
 */

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RegisterRemoteSourceTest {

    private lateinit var registerRemoteSource: RegisterRemoteSource

    private val mockWebServer = MockWebServer()
    private val service = Retrofit.Builder()
        .baseUrl(mockWebServer.url(""))
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(RegisterService::class.java)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        registerRemoteSource = RegisterRemoteSourceImpl(service)
    }

    @Test
    fun `register response success`() {
        runBlocking {
            val fileName = "register_response.json"
            mockWebServer.enqueueResponse(fileName)
            val fakeRegisterRes = RetrofitUtils.getBaseResponseFromJson(fileName)
            val registerRequest = RegisterRequest(
                email = "asdf@gmail.com",
                password = "asdfasdf",
                name = "asdf"
            )

            val registerResponse = registerRemoteSource.register(registerRequest)
            Assert.assertNotNull(registerResponse)
            Assert.assertTrue(registerResponse is Result.Success)
            Assert.assertEquals(
                fakeRegisterRes.message,
                (registerResponse as Result.Success).data.message
            )
        }
    }

    @Test
    fun `register response failed`() {
        runBlocking {
            val fakeErrorCode = 404
            mockWebServer.enqueueResponse(
                fileName = "register_email_validation_response.json",
                responseCode = fakeErrorCode
            )
            val registerRequest = RegisterRequest(
                email = "asdf@gmail.com",
                password = "asdfasdf",
                name = "asdf"
            )

            val registerRes = registerRemoteSource.register(registerRequest)
            Assert.assertNotNull(registerRes)
            Assert.assertTrue(registerRes is Result.Error)
            Assert.assertEquals(fakeErrorCode, (registerRes as Result.Error).responseCode)
        }
    }
}
