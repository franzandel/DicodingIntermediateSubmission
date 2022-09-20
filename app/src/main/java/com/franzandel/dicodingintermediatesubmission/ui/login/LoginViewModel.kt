package com.franzandel.dicodingintermediatesubmission.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.data.model.request.LoginRequest
import com.franzandel.dicodingintermediatesubmission.domain.usecase.LoginUseCase
import com.franzandel.dicodingintermediatesubmission.ui.login.model.LoginResult
import com.franzandel.dicodingintermediatesubmission.utils.onError
import com.franzandel.dicodingintermediatesubmission.utils.onException
import com.franzandel.dicodingintermediatesubmission.utils.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val coroutineThread: CoroutineThread
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch(coroutineThread.main) {
            _loading.value = true
            loginUseCase(loginRequest)
                .onSuccess {
                    _loading.value = false
                    _loginResult.value = LoginResult(success = Unit)
                }.onError { _, _ ->
                    _loading.value = false
                    _loginResult.value = LoginResult(error = R.string.login_failed)
                }.onException {
                    _loading.value = false
                    _loginResult.value = LoginResult(error = R.string.system_error)
                }
        }
    }
}
