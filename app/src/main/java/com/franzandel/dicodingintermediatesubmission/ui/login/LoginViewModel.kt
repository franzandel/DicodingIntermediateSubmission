package com.franzandel.dicodingintermediatesubmission.ui.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.base.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.base.model.Result
import com.franzandel.dicodingintermediatesubmission.data.model.LoginRequest
import com.franzandel.dicodingintermediatesubmission.domain.usecase.LoginUseCase
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val coroutineThread: CoroutineThread
) : ViewModel() {

    private val _usernameValidation = MutableLiveData<Int>()
    val usernameValidation: LiveData<Int> = _usernameValidation

    private val _passwordValidation = MutableLiveData<Int>()
    val passwordValidation: LiveData<Int> = _passwordValidation

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        validateUsername(username)
        validatePassword(password)
        if (usernameValidation.value == FORM_VALID && passwordValidation.value == FORM_VALID) {
            viewModelScope.launch(coroutineThread.main) {
                _loading.value = true
                val loginRequest = LoginRequest(
                    email = username,
                    password = password
                )
                when (loginUseCase(loginRequest)) {
                    is Result.Success -> {
                        _loading.value = false
                        _loginResult.value = LoginResult(success = Unit)
                    }
                    is Result.Error -> {
                        _loading.value = false
                        _loginResult.value = LoginResult(error = R.string.login_failed)
                    }
                    is Result.Exception -> {
                        _loading.value = false
                        _loginResult.value = LoginResult(error = R.string.system_error)
                    }
                }
            }
        }
    }

    fun validateUsername(username: String) {
        if (isUsernameEmpty(username)) {
            _usernameValidation.value = R.string.empty_username
        } else if (!isUsernameValid(username)) {
            _usernameValidation.value = R.string.invalid_username
        } else {
            _usernameValidation.value = FORM_VALID
        }
    }

    fun validatePassword(password: String) {
        if (!isPasswordValid(password)) {
            _passwordValidation.value = R.string.invalid_password
        } else {
            _passwordValidation.value = FORM_VALID
        }
    }

    private fun isUsernameValid(username: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(username).matches()
    }

    private fun isUsernameEmpty(username: String): Boolean {
        return username.isBlank()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    companion object {
        const val FORM_VALID = 0
    }
}
