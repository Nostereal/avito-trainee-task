package com.nostereal.avitotest

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition

import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import com.google.maps.android.clustering.ClusterManager
import com.nostereal.avitotest.models.PinsData
import com.nostereal.avitotest.models.ParcelableSet
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var clusterManager: ClusterManager<MapClusterItem>
    private lateinit var pinsData: PinsData

    private lateinit var servicesToShow: Set<String> // or HashSet if sequence doesn't matter

    companion object {
        const val FILTER_REQUEST_CODE: Int = 100
        const val SERVICE_SET_EXTRA_NAME = "Services"
        const val SERVICES_TO_SHOW_SET_EXTRA_NAME = "ServicesToShow"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragmentView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val inputStream = assets.open("pins.json")
        pinsData = inputStream.convertJsonToDataClass(PinsData::class)
        servicesToShow = pinsData.services.toSet()

        fabToFilterActivity.setOnClickListener {
            val intent = Intent(this, FilterActivity::class.java).apply {
                putExtra(SERVICE_SET_EXTRA_NAME, ParcelableSet(pinsData.services.toSet()))
                putExtra(SERVICES_TO_SHOW_SET_EXTRA_NAME, ParcelableSet(servicesToShow))
            }

            startActivityForResult(intent, FILTER_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILTER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_CANCELED) {
                Snackbar.make(
                    mapFragmentView.view!!,
                    "Filter wasn't saved",
                    Snackbar.LENGTH_SHORT
                ).show()
            }

            if (resultCode == Activity.RESULT_OK) {
                Log.d("MapsActivity", "Result is OK, filtering markers...")

                // get filtered services from FilterActivity
                Log.d(
                    "MapsActivity",
                    "parcelableExtra = ${data!!.getParcelableExtra<ParcelableSet>(FilterActivity.FILTERED_SERVICES_SET_RESULT_EXTRA)}"
                )
                val filteredServices =
                    data.getParcelableExtra<ParcelableSet>(FilterActivity.FILTERED_SERVICES_SET_RESULT_EXTRA)?.data
                        ?: servicesToShow

                if (filteredServices != servicesToShow) {
                    Log.d("MapsActivity", "Filtered services != servicesToShow")

                    // clear old markers from the map
                    clusterManager.clearItems()
                    map.clear()

                    servicesToShow = filteredServices
                    val filteredPins = pinsData.pins.filter { it.service in servicesToShow }

                    clusterManager.addClusterItemsFromList(filteredPins)
                    clusterManager.cluster() // force clusters update
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
        map = googleMap

        clusterManager = ClusterManager(this, map)

        val randomPinCoordinates = pinsData.pins.random().coordinates
        map.apply {
            setOnCameraIdleListener(clusterManager)
            setOnMarkerClickListener(clusterManager)

            moveCamera(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.fromLatLngZoom(
                        LatLng(
                            randomPinCoordinates.latitude,
                            randomPinCoordinates.longitude
                        ),
                        11f // zoom level
                    )
                )
            )

            // slide up fab because of map layout
            setOnMarkerClickListener {
                fabToFilterActivity.slideUpAnimation()
                false
            }

            // slide down fab
            setOnInfoWindowCloseListener {
                fabToFilterActivity.slideDownAnimation()
            }
            setOnMapClickListener {
                fabToFilterActivity.slideDownAnimation()
            }

        }

        clusterManager.addClusterItemsFromList(pinsData.pins)
    }
}
