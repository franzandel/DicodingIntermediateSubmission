package com.franzandel.dicodingintermediatesubmission.ui.addstory

import android.location.Location
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThreadImpl
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.domain.model.AddStory
import com.franzandel.dicodingintermediatesubmission.domain.usecase.UploadImageUseCase
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
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

/**
 * Created by Franz Andel
 * on 27 August 2022.
 */

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AddStoryViewModelTest {

    @Mock
    private lateinit var uploadImageUseCase: UploadImageUseCase

    @Mock
    private lateinit var fakeCurrentLocation: Location
    private lateinit var addStoryViewModel: AddStoryViewModel
    private val coroutineThread: CoroutineThread = CoroutineThreadImpl()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        addStoryViewModel = AddStoryViewModel(uploadImageUseCase, coroutineThread)
    }

    @Test
    fun `when uploadImage got validation`() = runTest {
        val fakeDescription = ""

        addStoryViewModel.validateDescription(fakeDescription)
        val actualAddStory = addStoryViewModel.descriptionValidation.getOrAwaitValue()
        Assert.assertNotNull(actualAddStory)
        Assert.assertEquals(R.string.add_story_empty_description, actualAddStory)
    }

    @Test
    fun `when uploadImage return success`() = runTest {
        val expectedAddStory = Result.Success(AddStory(error = false, "upload image success"))
        val fakeFile = File("asdf")
        val fakeDescription = "lalala"
        fakeCurrentLocation.latitude = 0.0
        fakeCurrentLocation.longitude = 0.0
        val fakeAddStoryRequest = addStoryViewModel.generateAddStoryRequest(fakeFile, fakeDescription, fakeCurrentLocation)

        Mockito.`when`(uploadImageUseCase(fakeAddStoryRequest)).thenReturn(expectedAddStory)

        addStoryViewModel.uploadImage(fakeAddStoryRequest)
        val actualAddStory = addStoryViewModel.uploadImageResult.getOrAwaitValue()
        Mockito.verify(uploadImageUseCase).invoke(fakeAddStoryRequest)
        Assert.assertNotNull(actualAddStory)
        Assert.assertNotNull(actualAddStory.success)
        Assert.assertEquals(R.string.add_story_success_upload, actualAddStory.success)
    }

    @Test
    fun `when uploadImage return failed`() = runTest {
        val expectedAddStory = Result.Error(400, AddStory(error = true, message = "upload image failed"))
        val fakeFile = File("asdf")
        val fakeDescription = "lalala"
        fakeCurrentLocation.latitude = 0.0
        fakeCurrentLocation.longitude = 0.0
        val fakeAddStoryRequest = addStoryViewModel.generateAddStoryRequest(fakeFile, fakeDescription, fakeCurrentLocation)

        Mockito.`when`(uploadImageUseCase(fakeAddStoryRequest)).thenReturn(expectedAddStory)

        addStoryViewModel.uploadImage(fakeAddStoryRequest)
        val actualLogin = addStoryViewModel.uploadImageResult.getOrAwaitValue()
        Mockito.verify(uploadImageUseCase).invoke(fakeAddStoryRequest)
        Assert.assertNotNull(actualLogin)
        Assert.assertNotNull(actualLogin.error)
        Assert.assertEquals(R.string.system_error, actualLogin.error)
    }

    @Test
    fun `when uploadImage return exception`() = runTest {
        val expectedAddStory = Result.Exception(Exception("unexpected error"))
        val fakeFile = File("asdf")
        val fakeDescription = "lalala"
        fakeCurrentLocation.latitude = 0.0
        fakeCurrentLocation.longitude = 0.0
        val fakeAddStoryRequest = addStoryViewModel.generateAddStoryRequest(fakeFile, fakeDescription, fakeCurrentLocation)

        Mockito.`when`(uploadImageUseCase(fakeAddStoryRequest)).thenReturn(expectedAddStory)

        addStoryViewModel.uploadImage(fakeAddStoryRequest)
        val actualAddStory = addStoryViewModel.uploadImageResult.getOrAwaitValue()
        Mockito.verify(uploadImageUseCase).invoke(fakeAddStoryRequest)
        Assert.assertNotNull(actualAddStory)
        Assert.assertNotNull(actualAddStory.error)
        Assert.assertEquals(R.string.system_error, actualAddStory.error)
    }
}
