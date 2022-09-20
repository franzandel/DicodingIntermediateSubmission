package com.franzandel.dicodingintermediatesubmission.utils.location

import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

/**
 * Created by Franz Andel
 * on 16 September 2022.
 */

abstract class LocationUtils(context: Context) {

    protected val mutableLocationSuccess = MutableLiveData<Location>()
    val onLocationSuccess: LiveData<Location> = mutableLocationSuccess

    protected val mutableOnLocationFailed = MutableLiveData<Unit>()
    val onLocationFailed: LiveData<Unit> = mutableOnLocationFailed

    protected val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    abstract fun getLocation()
}
