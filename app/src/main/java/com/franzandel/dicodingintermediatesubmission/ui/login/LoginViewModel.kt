package com.franzandel.dicodingintermediatesubmission.ui.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.data.LoginRepository
import com.franzandel.dicodingintermediatesubmission.data.Result

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _usernameValidation = MutableLiveData<Int>()
    val usernameValidation: LiveData<Int> = _usernameValidation

    private val _passwordValidation = MutableLiveData<Int>()
    val passwordValidation: LiveData<Int> = _passwordValidation

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(username, password)

        if (result is Result.Success) {
            _loginResult.value =
                LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
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
