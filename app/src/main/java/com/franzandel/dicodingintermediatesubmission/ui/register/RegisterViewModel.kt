package com.franzandel.dicodingintermediatesubmission.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.data.consts.ValidationConst
import com.franzandel.dicodingintermediatesubmission.data.model.request.RegisterRequest
import com.franzandel.dicodingintermediatesubmission.domain.model.Register
import com.franzandel.dicodingintermediatesubmission.domain.usecase.RegisterUseCase
import com.franzandel.dicodingintermediatesubmission.test.EspressoIdlingResource
import com.franzandel.dicodingintermediatesubmission.ui.register.model.RegisterResult
import com.franzandel.dicodingintermediatesubmission.utils.onError
import com.franzandel.dicodingintermediatesubmission.utils.onException
import com.franzandel.dicodingintermediatesubmission.utils.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Franz Andel
 * on 27 April 2022.
 */

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val useCase: RegisterUseCase,
    private val coroutineThread: CoroutineThread
) : ViewModel() {

    private val _passwordConfirmationValidation = MutableLiveData<Int>()
    val passwordConfirmationValidation: LiveData<Int> = _passwordConfirmationValidation

    private val _registerResult = MutableLiveData<RegisterResult>()
    val registerResult: LiveData<RegisterResult> = _registerResult

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun register(registerRequest: RegisterRequest, confirmationPassword: String) {
        if (validateConfirmationPassword(registerRequest.password, confirmationPassword)) {
            viewModelScope.launch(coroutineThread.main) {
                _loading.value = true
                useCase(registerRequest)
                    .onSuccess {
                        _loading.value = false
                        _registerResult.value = RegisterResult(success = Unit)
                    }.onError { _, register: Register? ->
                        _loading.value = false
                        if (register?.message.equals(EMAIL_ALREADY_TAKEN, ignoreCase = true)) {
                            _registerResult.value =
                                RegisterResult(error = R.string.register_already_exist)
                        } else {
                            _registerResult.value = RegisterResult(error = R.string.register_failed)
                        }
                    }.onException {
                        _loading.value = false
                        _registerResult.value = RegisterResult(error = R.string.system_error)
                    }
            }
        } else {
            EspressoIdlingResource.decrement()
        }
    }

    fun validateConfirmationPassword(password: String, confirmationPassword: String): Boolean {
        return when {
            confirmationPassword.isBlank() -> {
                _passwordConfirmationValidation.value = R.string.empty_confirmation_password
                false
            }
            password == confirmationPassword -> {
                _passwordConfirmationValidation.value = ValidationConst.FORM_VALID
                true
            }
            else -> {
                _passwordConfirmationValidation.value = R.string.invalid_confirmation_password
                false
            }
        }
    }

    companion object {
        const val EMAIL_ALREADY_TAKEN = "Email is already taken"
    }
}
