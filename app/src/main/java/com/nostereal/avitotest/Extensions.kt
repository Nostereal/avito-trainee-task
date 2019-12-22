package com.nostereal.avitotest

import android.view.animation.LinearInterpolator
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.nostereal.avitotest.models.Pin
import java.io.InputStream
import kotlin.reflect.KClass

inline fun <reified T : ClusterItem> ClusterManager<T>.addClusterItemsFromList(markers: List<Pin>) {
    markers
        .map { pin ->
            MapClusterItem(
                pin.coordinates.latitude,
                pin.coordinates.longitude,
                "Service: ${pin.service}"
            )
        }.forEach { clusterItem ->
            this.addItem(clusterItem as T)
        }
}

fun <T : Any> InputStream.convertJsonToDataClass(
    dataClass: Class<T>
): T {
    val json = this.bufferedReader().use { it.readText() }
    return Gson().fromJson(json, dataClass)
}

fun FloatingActionButton.slideUpAnimation() {
    this.animate().apply {
        duration = 300
        interpolator = LinearInterpolator()
        translationY(-130f)
    }.start()
}

fun FloatingActionButton.slideDownAnimation() {
    this.animate().apply {
        duration = 300
        interpolator = LinearInterpolator()
        translationY(0f)
    }
}