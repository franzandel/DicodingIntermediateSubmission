package com.franzandel.dicodingintermediatesubmission.ui.detail

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.utils.geolocation.GeolocationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Franz Andel
 * on 04 May 2022.
 */

@HiltViewModel
class DetailViewModel @Inject constructor(private val coroutineThread: CoroutineThread) :
    ViewModel() {

    private var _location = MutableLiveData<String>()
    val location: LiveData<String> = _location

    val isLocationAvailable = Transformations.map(_location) {
        it != null
    }

    fun getLocation(context: Context, latitude: Double?, longitude: Double?) {
        if (latitude == null || longitude == null) return
        viewModelScope.launch(coroutineThread.default) {
            _location.postValue(GeolocationUtils.getCountryState(context, latitude, longitude))
        }
    }
}
