package com.franzandel.dicodingintermediatesubmission.ui.addstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.base.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.data.Result
import com.franzandel.dicodingintermediatesubmission.data.model.AddStoryRequest
import com.franzandel.dicodingintermediatesubmission.domain.usecase.UploadImageUseCase
import com.franzandel.dicodingintermediatesubmission.ui.login.LoggedInUserView
import com.franzandel.dicodingintermediatesubmission.ui.login.LoginResult
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

/**
 * Created by Franz Andel <franz.andel@ovo.id>
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

    fun uploadImage(compressedFile: File, description: String) {
        viewModelScope.launch(coroutineThread.main) {
            _loading.value = true
            val descriptionRequestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = compressedFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
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
}
