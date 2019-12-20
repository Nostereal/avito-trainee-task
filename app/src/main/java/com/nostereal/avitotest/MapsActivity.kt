package com.nostereal.avitotest

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.nostereal.avitotest.models.Pin
import com.nostereal.avitotest.models.PinsData
import com.nostereal.avitotest.models.ParcelableSet
import kotlinx.android.synthetic.main.activity_maps.*
import java.io.InputStream
import kotlin.reflect.KClass

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var pinsData: PinsData

    private lateinit var servicesToShow: Set<String>

    companion object {
        const val FILTER_REQUEST_CODE: Int = 100
        const val SERVICE_SET_EXTRA_NAME = "Services"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val inputStream = assets.open("pins.json")
        pinsData = convertJsonToClass(inputStream, PinsData::class)
        servicesToShow = pinsData.services.toSet()

        fabToFilterActivity.setOnClickListener {
            val intent = Intent(this, FilterActivity::class.java).apply {
                putExtra(SERVICE_SET_EXTRA_NAME, ParcelableSet(pinsData.services.toSet()))
            }

            startActivityForResult(intent, FILTER_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data == null || resultCode == Activity.RESULT_CANCELED) {
            Snackbar.make(
                mapActivityRootView,
                "Nothing was selected",
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }

        if (requestCode == FILTER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d("MapsActivity", "Result is OK, filtering markers...")

                // get filtered services from FilterActivity

                Log.d("MapsActivity", "parcelableExtra = ${data.getParcelableExtra<ParcelableSet>(FilterActivity.FILTERED_SERVICES_SET_RESULT_EXTRA)}")
                val filteredServices =
                    data.getParcelableExtra<ParcelableSet>(FilterActivity.FILTERED_SERVICES_SET_RESULT_EXTRA)?.data
                        ?: servicesToShow

                if (filteredServices != servicesToShow) {
                    Log.d("MapsActivity", "Filtered services != servicesToShow")
                    mMap.clear()

                    servicesToShow = filteredServices
                    val filteredPins = pinsData.pins.filter { it.service in servicesToShow }

                    mMap.addMarkersFromList(filteredPins)
                }
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
        mMap = googleMap.apply { addMarkersFromList(pinsData.pins) }

        val randomPinCoordinates = pinsData.pins.random().coordinates
        mMap.moveCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.fromLatLngZoom(
                    LatLng(
                        randomPinCoordinates.latitude,
                        randomPinCoordinates.longitude
                    ),
                    11f // zoom
                )
            )
        )
    }

    private fun <T : Any> convertJsonToClass(
        inputStream: InputStream,
        dataClass: KClass<T>
    ): T {
        val json = inputStream.bufferedReader().use { it.readText() }
        return Gson().fromJson(json, dataClass.java)
    }


    /**
     * Adds all markers from the list to the specified map
     */
    private fun GoogleMap.addMarkersFromList(markers: List<Pin>) {
        markers.forEach { pin ->
            val coordinates = pin.coordinates
            val pos = LatLng(coordinates.latitude, coordinates.longitude)
            this.addMarker(MarkerOptions().position(pos).title("Service: ${pin.service}"))
        }

    }
}
