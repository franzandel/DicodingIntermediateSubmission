package com.franzandel.dicodingintermediatesubmission.ui.login

import android.util.Patterns
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.base.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.data.Result
import com.franzandel.dicodingintermediatesubmission.data.model.LoginRequest
import com.franzandel.dicodingintermediatesubmission.domain.usecase.GetTokenUseCase
import com.franzandel.dicodingintermediatesubmission.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val getTokenUseCase: GetTokenUseCase,
    private val coroutineThread: CoroutineThread
) : ViewModel() {

    private val _usernameValidation = MutableLiveData<Int>()
    val usernameValidation: LiveData<Int> = _usernameValidation

    private val _passwordValidation = MutableLiveData<Int>()
    val passwordValidation: LiveData<Int> = _passwordValidation

    private val _loadingVisibility = MutableLiveData<Int>()
    val loadingVisibility: LiveData<Int> = _loadingVisibility

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private var _token = MutableLiveData<String>()
    val token: LiveData<String> = _token

    fun login(username: String, password: String) {
        _loadingVisibility.value = View.VISIBLE
        validateUsername(username)
        validatePassword(password)
        if (usernameValidation.value == FORM_VALID && passwordValidation.value == FORM_VALID) {
            val loginRequest = LoginRequest(
                email = username,
                password = password
            )
            viewModelScope.launch(coroutineThread.main) {
                when (val result = loginUseCase.execute(loginRequest)) {
                    is Result.Success -> {
                        _loginResult.value =
                            LoginResult(success = LoggedInUserView(displayName = result.data.loginResult?.name.orEmpty()))
                    }
                    is Result.Error -> {
                        _loginResult.value = LoginResult(error = R.string.login_failed)
                    }
                    is Result.Exception -> {
                        _loginResult.value = LoginResult(error = R.string.system_error)
                    }
                }
            }
        }
        _loadingVisibility.value = View.GONE
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

    fun getToken() {
        viewModelScope.launch(coroutineThread.main) {
            when (val result = getTokenUseCase.execute()) {
                is Result.Success -> {
                    result.data.collect {
                        _token.value = it
                    }
                }
                is Result.Error -> TODO()
                is Result.Exception -> TODO()
            }
        }
    }

    companion object {
        const val FORM_VALID = 0
    }
}
