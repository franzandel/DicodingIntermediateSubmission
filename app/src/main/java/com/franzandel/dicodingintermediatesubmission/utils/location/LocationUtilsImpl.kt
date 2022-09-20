package com.franzandel.dicodingintermediatesubmission.utils.location

import android.annotation.SuppressLint
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

    @SuppressLint("MissingPermission")
    override fun getLocation() {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                mutableLocationSuccess.value = location
            } else {
                mutableOnLocationFailed.value = Unit
            }
        }
    }
}
