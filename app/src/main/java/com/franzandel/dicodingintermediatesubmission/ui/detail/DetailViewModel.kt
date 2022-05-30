package com.franzandel.dicodingintermediatesubmission.ui.detail

import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Franz Andel
 * on 04 May 2022.
 */

@HiltViewModel
class DetailViewModel @Inject constructor(private val coroutineThread: CoroutineThread) : ViewModel() {

    private var _location = MutableLiveData<String>()
    val location: LiveData<String> = _location

    val isLocationAvailable = Transformations.map(_location) {
        it != null
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    fun getLocation(geocoder: Geocoder, latitude: Double?, longitude: Double?) {
        if (latitude == null || longitude == null) return
        viewModelScope.launch(coroutineThread.background) {
            val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)

            if (addresses.isNotEmpty()) {
                val address = addresses[0]
                val state = address.adminArea
                val country = address.countryName
                _location.postValue("$country, $state")
            }
        }
    }
}
