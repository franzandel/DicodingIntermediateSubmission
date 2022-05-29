package com.franzandel.dicodingintermediatesubmission.ui.home

import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.base.model.Navigation
import com.franzandel.dicodingintermediatesubmission.data.consts.IntentConst
import com.franzandel.dicodingintermediatesubmission.domain.model.Story
import com.franzandel.dicodingintermediatesubmission.domain.usecase.ClearStorageUseCase
import com.franzandel.dicodingintermediatesubmission.domain.usecase.GetPagingStoriesUseCase
import com.franzandel.dicodingintermediatesubmission.ui.detail.DetailActivity
import com.franzandel.dicodingintermediatesubmission.utils.onError
import com.franzandel.dicodingintermediatesubmission.utils.onException
import com.franzandel.dicodingintermediatesubmission.utils.onSuccess
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by Franz Andel
 * on 02 May 2022.
 */

class HomeViewModel(
    private val getPagingStoriesUseCase: GetPagingStoriesUseCase,
    private val clearStorageUseCase: ClearStorageUseCase,
    private val coroutineThread: CoroutineThread
) : ViewModel() {

    private var _homeResult = MutableLiveData<HomeResult>()
    val homeResult: LiveData<HomeResult> = _homeResult

    private var _navigateTo = MutableLiveData<Navigation>()
    val navigateTo: LiveData<Navigation> = _navigateTo

    private var _clearStorageResult = MutableLiveData<Boolean>()
    val clearStorageResult: LiveData<Boolean> = _clearStorageResult

    fun getStories() {
        viewModelScope.launch(coroutineThread.main) {
            getPagingStoriesUseCase()
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
        _navigateTo.value = Navigation(
            destination = DetailActivity::class.java,
            bundle = bundleOf(
                Pair(IntentConst.EXTRA_STORY_DETAIL, HomeDetailMapper.transform(story))
            )
        )
    }

    fun clearStorage() {
        viewModelScope.launch(coroutineThread.main) {
            clearStorageUseCase(Unit)
                .onSuccess {
                    _clearStorageResult.value = true
                }.onError { _, _ ->
                    _clearStorageResult.value = false
                }.onException {
                    _clearStorageResult.value = false
                }
        }
    }
}
