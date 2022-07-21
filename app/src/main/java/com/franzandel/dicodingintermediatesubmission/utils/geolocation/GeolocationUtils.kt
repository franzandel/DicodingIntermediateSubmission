package com.franzandel.dicodingintermediatesubmission.utils.geolocation

import android.location.Address
import android.location.Geocoder

/**
 * Created by Franz Andel <franz.andel@ovo.id>
 * on 21 July 2022.
 */

object GeolocationUtils {

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun getCountryState(
        geocoder: Geocoder,
        latitude: Double,
        longitude: Double
    ): String? {
        val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)

        if (addresses.isNotEmpty()) {
            val address = addresses[0]
            val state = address.adminArea
            val country = address.countryName
            return "$country, $state"
        }

        return null
    }
}
