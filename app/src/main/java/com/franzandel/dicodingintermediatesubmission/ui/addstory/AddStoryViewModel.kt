package com.franzandel.dicodingintermediatesubmission.ui.addstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.base.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.base.model.Result
import com.franzandel.dicodingintermediatesubmission.data.model.AddStoryRequest
import com.franzandel.dicodingintermediatesubmission.domain.usecase.UploadImageUseCase
import com.franzandel.dicodingintermediatesubmission.ui.login.LoginViewModel
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

/**
 * Created by Franz Andel
 * on 07 May 2022.
 */

class AddStoryViewModel(
    private val uploadImageUseCase: UploadImageUseCase,
    private val coroutineThread: CoroutineThread
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _uploadImageResult = MutableLiveData<AddStoryResult>()
    val uploadImageResult: LiveData<AddStoryResult> = _uploadImageResult

    private val _descriptionValidation = MutableLiveData<Int>()
    val descriptionValidation: LiveData<Int> = _descriptionValidation

    suspend fun uploadImage(compressedFile: File, description: String) {
        withContext(coroutineThread.main) {
            if (validateDescription(description)) {
                _loading.value = true
                val descriptionRequestBody = description.toRequestBody("text/plain".toMediaType())
                val requestImageFile =
                    compressedFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    compressedFile.name,
                    requestImageFile
                )
                val addStoryRequest = AddStoryRequest(
                    file = imageMultipart,
                    description = descriptionRequestBody
                )
                when (uploadImageUseCase.execute(addStoryRequest)) {
                    is Result.Success -> {
                        _loading.value = false
                        _uploadImageResult.value =
                            AddStoryResult(success = R.string.add_story_success_upload)
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
    }

    fun validateDescription(description: String): Boolean {
        return if (description.isBlank()) {
            _descriptionValidation.value = R.string.add_story_empty_description
            false
        } else {
            _descriptionValidation.value = LoginViewModel.FORM_VALID
            true
        }
    }

    companion object {
        const val FORM_VALID = 0
    }
}
