package com.franzandel.dicodingintermediatesubmission.utils.geolocation

import android.content.Context
import android.location.Geocoder
import java.util.Locale

/**
 * Created by Franz Andel
 * on 21 July 2022.
 */

object GeolocationUtils {

    @Suppress("DEPRECATION")
    fun getCountryState(
        context: Context,
        latitude: Double,
        longitude: Double
    ): String? {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)

            if (addresses?.isNotEmpty() == true) {
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
