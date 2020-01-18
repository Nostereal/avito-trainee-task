package com.nostereal.avitotest

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class MapClusterItem(
    private val lat: Double,
    private val lng: Double,
    val service: String,
    private val title: String? = null,
    private val snippet: String? = null
) : ClusterItem {

    override fun getSnippet(): String? = snippet

    override fun getTitle(): String? = title

    override fun getPosition(): LatLng = LatLng(lat, lng)
}