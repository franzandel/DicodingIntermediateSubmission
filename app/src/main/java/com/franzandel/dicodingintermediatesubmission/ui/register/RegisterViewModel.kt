package com.franzandel.dicodingintermediatesubmission.ui.register

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.ui.login.LoginViewModel

/**
 * Created by Franz Andel
 * on 27 April 2022.
 */

class RegisterViewModel : ViewModel() {

    private val _nameValidation = MutableLiveData<Int>()
    val nameValidation: LiveData<Int> = _nameValidation

    private val _usernameValidation = MutableLiveData<Int>()
    val usernameValidation: LiveData<Int> = _usernameValidation

    private val _passwordValidation = MutableLiveData<Int>()
    val passwordValidation: LiveData<Int> = _passwordValidation

    private val _passwordConfirmationValidation = MutableLiveData<Int>()
    val passwordConfirmationValidation: LiveData<Int> = _passwordConfirmationValidation

    private val _nextResult = MutableLiveData<Boolean>()
    val nextResult: LiveData<Boolean> = _nextResult

    fun next(name: String, username: String, password: String, confirmationPassword: String) {
        if (validateName(name) &&
            validateUsername(username) &&
            validatePassword(password) &&
            validateConfirmationPassword(password, confirmationPassword)
        ) {
            _nextResult.value = true
        } else {
            _nextResult.value = false
        }
    }

    fun validateName(name: String): Boolean {
        return if (isNameEmpty(name)) {
            _nameValidation.value = R.string.empty_name
            false
        } else {
            _nameValidation.value = FORM_VALID
            true
        }
    }

    private fun isNameEmpty(name: String): Boolean {
        return name.isBlank()
    }

    fun validateUsername(username: String): Boolean {
        return if (username.isBlank()) {
            _usernameValidation.value = R.string.empty_username
            false
        } else if (!isUsernameValid(username)) {
            _usernameValidation.value = R.string.invalid_username
            false
        } else {
            _usernameValidation.value = LoginViewModel.FORM_VALID
            true
        }
    }

    private fun isUsernameValid(username: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(username).matches()
    }

    fun validatePassword(password: String): Boolean {
        return if (!isPasswordValid(password)) {
            _passwordValidation.value = R.string.invalid_password
            false
        } else {
            _passwordValidation.value = FORM_VALID
            true
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    fun validateConfirmationPassword(password: String, confirmationPassword: String): Boolean {
        return when {
            confirmationPassword.isBlank() -> {
                _passwordConfirmationValidation.value = R.string.empty_confirmation_password
                false
            }
            password == confirmationPassword -> {
                _passwordConfirmationValidation.value = FORM_VALID
                true
            }
            else -> {
                _passwordConfirmationValidation.value = R.string.invalid_confirmation_password
                false
            }
        }
    }

    companion object {
        const val FORM_VALID = 0
    }
}
