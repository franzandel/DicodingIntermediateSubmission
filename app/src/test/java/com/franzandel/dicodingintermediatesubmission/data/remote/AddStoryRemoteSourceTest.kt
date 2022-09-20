package com.franzandel.dicodingintermediatesubmission.data.remote

import com.franzandel.dicodingintermediatesubmission.data.consts.FileConst
import com.franzandel.dicodingintermediatesubmission.data.model.request.AddStoryRequest
import com.franzandel.dicodingintermediatesubmission.data.service.AddStoryService
import com.franzandel.dicodingintermediatesubmission.helper.MainDispatcherRule
import com.franzandel.dicodingintermediatesubmission.test.enqueueResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

/**
 * Created by Franz Andel
 * on 20 September 2022.
 */

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AddStoryRemoteSourceTest {

    private lateinit var addStoryRemoteSource: AddStoryRemoteSource

    private val mockWebServer = MockWebServer()
    private val service = Retrofit.Builder()
        .baseUrl(mockWebServer.url(""))
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(AddStoryService::class.java)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        addStoryRemoteSource = AddStoryRemoteSourceImpl(service)
    }

    @Test
    fun `add story response success`() {
        runBlocking {
            val fileName = "add_story_response.json"
            mockWebServer.enqueueResponse(fileName)
            val fakeToken = "asdf"
            val fakeAddStoryRequest = getFakeAddStoryRequest()

            val addStoryResponse = addStoryRemoteSource.uploadImage(fakeToken, fakeAddStoryRequest)
            Assert.assertNotNull(addStoryResponse)
        }
    }

    @Test
    fun `add story response failed`() {
        runBlocking {
            val fakeErrorCode = 404
            mockWebServer.enqueueResponse(
                fileName = "add_story_error_response.json",
                responseCode = fakeErrorCode
            )
            val fakeToken = "asdf"
            val fakeAddStoryRequest = getFakeAddStoryRequest()

            val addStoryResponse = addStoryRemoteSource.uploadImage(fakeToken, fakeAddStoryRequest)
            Assert.assertNotNull(addStoryResponse)
        }
    }

    private fun getFakeAddStoryRequest(): AddStoryRequest {
        val fakeFile = File("asdf")
        val descriptionRequestBody = "description".toRequestBody(FileConst.TEXT_PLAIN.toMediaType())
        val requestImageFile = fakeFile.asRequestBody(FileConst.IMAGE_JPEG.toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            FileConst.PHOTO,
            fakeFile.name,
            requestImageFile
        )
        val latitudeRequestBody = "0.0".toRequestBody(FileConst.TEXT_PLAIN.toMediaType())
        val longitudeRequestBody = "0.0".toRequestBody(FileConst.TEXT_PLAIN.toMediaType())
        return AddStoryRequest(
            file = imageMultipart,
            description = descriptionRequestBody,
            latitude = latitudeRequestBody,
            longitude = longitudeRequestBody
        )
    }
}
