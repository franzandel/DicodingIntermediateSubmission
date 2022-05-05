package com.franzandel.dicodingintermediatesubmission.ui.home

import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.base.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.base.model.Navigation
import com.franzandel.dicodingintermediatesubmission.data.Result
import com.franzandel.dicodingintermediatesubmission.domain.model.Story
import com.franzandel.dicodingintermediatesubmission.domain.usecase.GetStoriesUseCase
import com.franzandel.dicodingintermediatesubmission.ui.detail.DetailActivity
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

    private var _navigateTo = MutableLiveData<Navigation>()
    val navigateTo: LiveData<Navigation> = _navigateTo

    fun getStories() {
        viewModelScope.launch(coroutineThread.main) {
            when (val result = getStoriesUseCase.execute()) {
                is Result.Success -> {
                    result.data.cachedIn(viewModelScope).collect {
                        _homeResult.value = HomeResult(success = it)
                    }
                }
                // error only handle if loginRepository.getToken() failed, rest already handled in adapter.loadStateFlow (HomeActivity)
                is Result.Error -> _homeResult.value = HomeResult(error = R.string.system_error)
                is Result.Exception -> _homeResult.value = HomeResult(error = R.string.system_error)
            }
        }
    }

    fun onClick(story: Story) {
        _navigateTo.value = Navigation(
            destination = DetailActivity::class.java,
            bundle = bundleOf(
                Pair(EXTRA_STORY_DETAIL, HomeDetailMapper.transform(story))
            )
        )
    }

    companion object {
        const val EXTRA_STORY_DETAIL = "extra_story_detail"
    }
}
