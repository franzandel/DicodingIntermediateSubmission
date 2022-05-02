package com.franzandel.dicodingintermediatesubmission.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.franzandel.dicodingintermediatesubmission.base.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.data.Result
import com.franzandel.dicodingintermediatesubmission.domain.usecase.GetStoriesUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by Franz Andel
 * on 02 May 2022.
 */

class HomeViewModel(
    private val getStoriesUseCase: GetStoriesUseCase,
    private val coroutineThread: CoroutineThread
) : ViewModel() {

    private var _homeResult = MutableLiveData<HomeResult>()
    val homeResult: LiveData<HomeResult> = _homeResult

    fun getStories() {
        viewModelScope.launch(coroutineThread.main) {
            when (val result = getStoriesUseCase.execute()) {
                is Result.Success -> {
                    result.data.cachedIn(viewModelScope).collect {
                        _homeResult.value = HomeResult(success = it)
                    }
                }
                is Result.Error -> _homeResult.value = HomeResult(error = 0)
                is Result.Exception -> _homeResult.value = HomeResult(error = 0)
            }
        }
    }
}
