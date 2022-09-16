package com.franzandel.dicodingintermediatesubmission.utils

import android.content.Context
import android.location.Location
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

/**
 * Created by Franz Andel
 * on 16 September 2022.
 */

class LocationUtilsImpl @Inject constructor(@ActivityContext context: Context) :
    LocationUtils(context) {

    override fun getLocation() {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                _onLocationSuccess.value = location
            } else {
                _onLocationFailed.value = Unit
            }
        }
    }
}
