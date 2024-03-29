package com.franzandel.dicodingintermediatesubmission.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.domain.model.Story
import com.franzandel.dicodingintermediatesubmission.domain.usecase.ClearStorageUseCase
import com.franzandel.dicodingintermediatesubmission.domain.usecase.GetLocationPreferenceUseCase
import com.franzandel.dicodingintermediatesubmission.domain.usecase.GetPagingStoriesUseCase
import com.franzandel.dicodingintermediatesubmission.domain.usecase.SetLocationPreferenceUseCase
import com.franzandel.dicodingintermediatesubmission.ui.detail.model.StoryDetail
import com.franzandel.dicodingintermediatesubmission.ui.home.mapper.HomeDetailMapper
import com.franzandel.dicodingintermediatesubmission.ui.home.model.HomeResult
import com.franzandel.dicodingintermediatesubmission.utils.onError
import com.franzandel.dicodingintermediatesubmission.utils.onException
import com.franzandel.dicodingintermediatesubmission.utils.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Franz Andel
 * on 02 May 2022.
 */

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPagingStoriesUseCase: GetPagingStoriesUseCase,
    private val clearStorageUseCase: ClearStorageUseCase,
    private val getLocationPreferenceUseCase: GetLocationPreferenceUseCase,
    private val setLocationPreferenceUseCase: SetLocationPreferenceUseCase,
    private val coroutineThread: CoroutineThread
) : ViewModel() {

    private var _homeResult = MutableLiveData<HomeResult>()
    val homeResult: LiveData<HomeResult> = _homeResult

    private var _navigateToDetail = MutableLiveData<StoryDetail>()
    val navigateToDetail: LiveData<StoryDetail> = _navigateToDetail

    private var _clearStorageResult = MutableLiveData<Boolean>()
    val clearStorageResult: LiveData<Boolean> = _clearStorageResult

    private var _locationPreference = MutableLiveData<Int>()
    val locationPreference: LiveData<Int> = _locationPreference

    fun getStories(location: Int) {
        viewModelScope.launch(coroutineThread.main) {
            getPagingStoriesUseCase(location)
                .onSuccess { pagingStory ->
                    pagingStory.cachedIn(viewModelScope).collect {
                        _homeResult.value = HomeResult(success = it)
                    }
                }.onError { _, _ ->
                    // error only handle if loginRepository.getToken() failed, rest already handled in adapter.loadStateFlow (HomeActivity)
                    _homeResult.value = HomeResult(error = R.string.system_error)
                }.onException {
                    _homeResult.value = HomeResult(error = R.string.system_error)
                }
        }
    }

    fun onClick(story: Story) {
        _navigateToDetail.value = HomeDetailMapper.transform(story)
    }

    fun clearStorage() {
        viewModelScope.launch(coroutineThread.main) {
            clearStorageUseCase()
                .onSuccess {
                    _clearStorageResult.value = true
                }.onError { _, _ ->
                    _clearStorageResult.value = false
                }.onException {
                    _clearStorageResult.value = false
                }
        }
    }

    fun getLocationPreference() {
        viewModelScope.launch(coroutineThread.main) {
            getLocationPreferenceUseCase()
                .onSuccess { result ->
                    result.collect {
                        _locationPreference.value = it
                    }
                }.onError { _, _ ->
                    _locationPreference.value = HomeActivity.ALL_STORIES
                }.onException {
                    _locationPreference.value = HomeActivity.ALL_STORIES
                }
        }
    }

    fun setLocationPreference(locationPreference: Int) {
        viewModelScope.launch(coroutineThread.main) {
            setLocationPreferenceUseCase(locationPreference)
        }
    }
}
