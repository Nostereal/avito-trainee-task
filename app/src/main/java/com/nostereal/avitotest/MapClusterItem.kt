package com.nostereal.avitotest

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class MapClusterItem(private val lat: Double, private val lng: Double) : ClusterItem {

    private var title: String? = null
    private var snippet: String? = null

    constructor(lat: Double, lng: Double, title: String) : this(lat, lng) { this.title = title}

    constructor(lat: Double, lng: Double, title: String, snippet: String) : this(lat, lng) {
        this.title = title
        this.snippet = snippet
    }

    override fun getSnippet(): String? = snippet

    override fun getTitle(): String? = title

    override fun getPosition(): LatLng = LatLng(lat, lng)
}