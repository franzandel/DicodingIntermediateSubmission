package com.franzandel.dicodingintermediatesubmission.utils.geolocation

import android.location.Address
import android.location.Geocoder
import java.lang.Exception

/**
 * Created by Franz Andel
 * on 21 July 2022.
 */

object GeolocationUtils {

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun getCountryState(
        geocoder: Geocoder,
        latitude: Double,
        longitude: Double
    ): String? {
        return try {
            val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)

            if (addresses.isNotEmpty()) {
                val address = addresses[0]
                val state = address.adminArea
                val country = address.countryName
                "$country, $state"
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
