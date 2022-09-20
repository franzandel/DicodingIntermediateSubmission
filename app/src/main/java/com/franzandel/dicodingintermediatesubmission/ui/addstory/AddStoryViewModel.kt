package com.franzandel.dicodingintermediatesubmission.ui.addstory

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.data.consts.FileConst
import com.franzandel.dicodingintermediatesubmission.data.consts.ValidationConst
import com.franzandel.dicodingintermediatesubmission.data.model.request.AddStoryRequest
import com.franzandel.dicodingintermediatesubmission.domain.usecase.UploadImageUseCase
import com.franzandel.dicodingintermediatesubmission.ui.addstory.model.AddStoryResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

/**
 * Created by Franz Andel
 * on 07 May 2022.
 */

@HiltViewModel
class AddStoryViewModel @Inject constructor(
    private val uploadImageUseCase: UploadImageUseCase,
    private val coroutineThread: CoroutineThread
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _uploadImageResult = MutableLiveData<AddStoryResult>()
    val uploadImageResult: LiveData<AddStoryResult> = _uploadImageResult

    private val _descriptionValidation = MutableLiveData<Int>()
    val descriptionValidation: LiveData<Int> = _descriptionValidation

    suspend fun uploadImage(addStoryRequest: AddStoryRequest) {
        withContext(coroutineThread.main) {
            _loading.value = true
            when (uploadImageUseCase(addStoryRequest)) {
                is Result.Success -> {
                    _loading.value = false
                    _uploadImageResult.value = AddStoryResult(success = R.string.add_story_success_upload)
                }
                is Result.Error -> {
                    _loading.value = false
                    _uploadImageResult.value = AddStoryResult(error = R.string.system_error)
                }
                is Result.Exception -> {
                    _loading.value = false
                    _uploadImageResult.value = AddStoryResult(error = R.string.system_error)
                }
            }
        }
    }

    fun generateAddStoryRequest(
        compressedFile: File,
        description: String,
        currentLocation: Location?
    ): AddStoryRequest {
        val descriptionRequestBody = description.toRequestBody(FileConst.TEXT_PLAIN.toMediaType())
        val requestImageFile =
            compressedFile.asRequestBody(FileConst.IMAGE_JPEG.toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            FileConst.PHOTO,
            compressedFile.name,
            requestImageFile
        )
        var latitudeRequestBody: RequestBody? = null
        var longitudeRequestBody: RequestBody? = null
        currentLocation?.let {
            latitudeRequestBody =
                it.latitude.toString().toRequestBody(FileConst.TEXT_PLAIN.toMediaType())
            longitudeRequestBody =
                it.longitude.toString().toRequestBody(FileConst.TEXT_PLAIN.toMediaType())
        }

        return AddStoryRequest(
            file = imageMultipart,
            description = descriptionRequestBody,
            latitude = latitudeRequestBody,
            longitude = longitudeRequestBody
        )
    }

    fun validateDescription(description: String): Boolean {
        return if (description.isBlank()) {
            _descriptionValidation.value = R.string.add_story_empty_description
            false
        } else {
            _descriptionValidation.value = ValidationConst.FORM_VALID
            true
        }
    }
}
