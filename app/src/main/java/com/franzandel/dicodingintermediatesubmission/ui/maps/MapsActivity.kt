package com.franzandel.dicodingintermediatesubmission.ui.maps

import android.content.res.Resources
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.Locale

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_map_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }
            R.id.hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setMapStyle()

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        val sydney = LatLng(-34.0, 151.0)

        mMap.addMarker(
            MarkerOptions()
                .position(sydney)
                .title(getAddressName(sydney))
                .snippet(getAdminArea(sydney))
        )
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15f))
    }

    private fun getAddressName(latLng: LatLng): String? {
        val address = getAddress(latLng)
        return address?.getAddressLine(0)
    }

    private fun getAdminArea(latLng: LatLng): String? {
        val address = getAddress(latLng)
        return address?.adminArea
    }

    private fun getAddress(latLng: LatLng): Address? {
        val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())
        try {
            return geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1).firstOrNull()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_aubergine_style))
            if (!success) {
                Log.e("TAG", "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e("TAG", "Can't find style. Error: ", exception)
        }
    }
}
