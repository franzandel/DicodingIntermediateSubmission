package com.franzandel.dicodingintermediatesubmission.ui.splashscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.franzandel.dicodingintermediatesubmission.base.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.domain.model.Login
import com.franzandel.dicodingintermediatesubmission.domain.usecase.GetTokenUseCase
import com.franzandel.dicodingintermediatesubmission.utils.onError
import com.franzandel.dicodingintermediatesubmission.utils.onException
import com.franzandel.dicodingintermediatesubmission.utils.onSuccess
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
            getTokenUseCase()
                .onSuccess { result ->
                    result.collect {
                        _isTokenEmpty.value = it.isEmpty()
                    }
                }.onError { _, _ ->
                    _isTokenEmpty.value = true
                }.onException {
                    _isTokenEmpty.value = true
                }
        }
    }
}
