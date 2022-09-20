package com.franzandel.dicodingintermediatesubmission.domain.usecase

import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThreadImpl
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.data.consts.FileConst
import com.franzandel.dicodingintermediatesubmission.data.model.request.AddStoryRequest
import com.franzandel.dicodingintermediatesubmission.domain.model.AddStory
import com.franzandel.dicodingintermediatesubmission.domain.repository.AddStoryRepository
import com.franzandel.dicodingintermediatesubmission.helper.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

/**
 * Created by Franz Andel
 * on 28 August 2022.
 */

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class UploadImageUseCaseTest {

    @Mock
    private lateinit var addStoryRepository: AddStoryRepository

    @Mock
    private lateinit var getTokenUseCase: GetTokenUseCase

    private lateinit var uploadImageUseCase: UploadImageUseCase
    private val coroutineThread: CoroutineThread = CoroutineThreadImpl()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        uploadImageUseCase =
            UploadImageUseCase(addStoryRepository, getTokenUseCase, coroutineThread)
    }

    @Test
    fun `when uploadImage return success`() = runTest {
        val expectedUploadImage =
            Result.Success(AddStory(error = false, message = "upload image success"))
        val fakeToken = "asdf"
        val fakeAddStoryRequest = getFakeAddStoryRequest()

        Mockito.`when`(getTokenUseCase()).thenReturn(Result.Success(flowOf(fakeToken)))
        Mockito.`when`(addStoryRepository.uploadImage(fakeToken, fakeAddStoryRequest))
            .thenReturn(expectedUploadImage)

        val actualUploadImage = uploadImageUseCase(fakeAddStoryRequest)
        Mockito.verify(addStoryRepository).uploadImage(fakeToken, fakeAddStoryRequest)
        Assert.assertNotNull(actualUploadImage)
        Assert.assertTrue(actualUploadImage is Result.Success)
        Assert.assertEquals(
            (actualUploadImage as Result.Success).data.message,
            expectedUploadImage.data.message
        )
    }

    @Test
    fun `when uploadImage return failed`() = runTest {
        val fakeResponseCode = 400
        val expectedUploadImage =
            Result.Error(fakeResponseCode, AddStory(error = true, message = "upload image failed"))
        val fakeToken = "asdf"
        val fakeAddStoryRequest = getFakeAddStoryRequest()

        Mockito.`when`(getTokenUseCase()).thenReturn(Result.Success(flowOf(fakeToken)))
        Mockito.`when`(addStoryRepository.uploadImage(fakeToken, fakeAddStoryRequest))
            .thenReturn(expectedUploadImage)

        val actualUploadImage = uploadImageUseCase(fakeAddStoryRequest)
        Mockito.verify(addStoryRepository).uploadImage(fakeToken, fakeAddStoryRequest)
        Assert.assertNotNull(actualUploadImage)
        Assert.assertTrue(actualUploadImage is Result.Error)
        Assert.assertEquals((actualUploadImage as Result.Error).responseCode, fakeResponseCode)
    }

    @Test
    fun `when uploadImage return exception`() = runTest {
        val fakeExceptionMessage = "uploadImage exception"
        val expectedUploadImage = Result.Exception(Exception(fakeExceptionMessage))
        val fakeToken = "asdf"
        val fakeAddStoryRequest = getFakeAddStoryRequest()

        Mockito.`when`(getTokenUseCase()).thenReturn(Result.Success(flowOf(fakeToken)))
        Mockito.`when`(addStoryRepository.uploadImage(fakeToken, fakeAddStoryRequest))
            .thenReturn(expectedUploadImage)

        val actualUploadImage = uploadImageUseCase(fakeAddStoryRequest)
        Mockito.verify(addStoryRepository).uploadImage(fakeToken, fakeAddStoryRequest)
        Assert.assertNotNull(actualUploadImage)
        Assert.assertTrue(actualUploadImage is Result.Exception)
        Assert.assertEquals(
            (actualUploadImage as Result.Exception).throwable.message,
            fakeExceptionMessage
        )
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
