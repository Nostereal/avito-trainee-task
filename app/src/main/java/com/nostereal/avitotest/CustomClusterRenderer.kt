package com.nostereal.avitotest

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class CustomClusterRenderer(
    context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<MapClusterItem>,
    private val services: Set<String>
) : DefaultClusterRenderer<MapClusterItem>(context, map, clusterManager) {

    override fun onBeforeClusterItemRendered(item: MapClusterItem, markerOptions: MarkerOptions) {

        // avoid ArithmeticException (smth / 0)
        if (services.isEmpty()) return

        val currentService = item.service
        val currentServiceIndex = services.indexOf(currentService)

        // set custom color depending on the service
        val descriptor =
            BitmapDescriptorFactory.defaultMarker(360f / services.size * currentServiceIndex)
        markerOptions.icon(descriptor)
    }
}