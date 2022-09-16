package com.franzandel.dicodingintermediatesubmission.utils

import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationServices

/**
 * Created by Franz Andel
 * on 16 September 2022.
 */

abstract class LocationUtils(context: Context) {

    protected val _onLocationSuccess = MutableLiveData<Location>()
    val onLocationSuccess: LiveData<Location> = _onLocationSuccess

    protected val _onLocationFailed = MutableLiveData<Unit>()
    val onLocationFailed: LiveData<Unit> = _onLocationFailed

    protected val fusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    abstract fun getLocation()
}
