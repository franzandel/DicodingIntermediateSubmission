package com.franzandel.dicodingintermediatesubmission.ui.name

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.franzandel.dicodingintermediatesubmission.R

/**
 * Created by Franz Andel
 * on 27 April 2022.
 */

class NameViewModel : ViewModel() {

    private val _nameValidation = MutableLiveData<Int>()
    val nameValidation: LiveData<Int> = _nameValidation

    private val _nextResult = MutableLiveData<Boolean>()
    val nextResult: LiveData<Boolean> = _nextResult

    fun validateName(name: String): Boolean {
        return if (isNameEmpty(name)) {
            _nameValidation.value = R.string.empty_name
            false
        } else {
            _nameValidation.value = FORM_VALID
            true
        }
    }

    fun next(name: String) {
        if (validateName(name)) {
            _nextResult.value = true
        } else {
            _nextResult.value = false
        }
    }

    private fun isNameEmpty(name: String): Boolean {
        return name.isBlank()
    }

    companion object {
        const val FORM_VALID = 0
    }
}
