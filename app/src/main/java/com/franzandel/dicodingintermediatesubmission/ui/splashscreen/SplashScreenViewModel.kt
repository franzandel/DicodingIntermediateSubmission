package com.franzandel.dicodingintermediatesubmission.ui.splashscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.franzandel.dicodingintermediatesubmission.base.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.data.Result
import com.franzandel.dicodingintermediatesubmission.domain.usecase.GetTokenUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by Franz Andel
 * on 02 May 2022.
 */

class SplashScreenViewModel(
    private val getTokenUseCase: GetTokenUseCase,
    private val coroutineThread: CoroutineThread
) : ViewModel() {

    private var _isTokenEmpty = MutableLiveData<Boolean>()
    val isTokenEmpty: LiveData<Boolean> = _isTokenEmpty

    fun getToken() {
        viewModelScope.launch(coroutineThread.main) {
            when (val result = getTokenUseCase.execute()) {
                is Result.Success -> {
                    result.data.collect {
                        _isTokenEmpty.value = it.isEmpty()
                    }
                }
                is Result.Error -> _isTokenEmpty.value = true
                is Result.Exception -> _isTokenEmpty.value = true
            }
        }
    }
}
