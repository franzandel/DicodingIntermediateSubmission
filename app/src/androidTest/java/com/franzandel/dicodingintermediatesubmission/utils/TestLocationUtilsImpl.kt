package com.franzandel.dicodingintermediatesubmission.utils

import android.content.Context
import android.location.Location
import com.franzandel.dicodingintermediatesubmission.BuildConfig
import com.franzandel.dicodingintermediatesubmission.utils.location.LocationUtils
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

/**
 * Created by Franz Andel
 * on 16 September 2022.
 */

class TestLocationUtilsImpl @Inject constructor(@ActivityContext context: Context) :
    LocationUtils(context) {

    override fun getLocation() {
        fusedLocationProviderClient.setMockMode(BuildConfig.DEBUG)

        val mockLocation = Location("UK")
        mockLocation.latitude = 51.507351
        mockLocation.longitude = -0.127758

        fusedLocationProviderClient.setMockLocation(mockLocation)
            .addOnSuccessListener {
                mutableLocationSuccess.value = mockLocation
            }
            .addOnFailureListener {
                mutableOnLocationFailed.value = Unit
            }
    }
}
