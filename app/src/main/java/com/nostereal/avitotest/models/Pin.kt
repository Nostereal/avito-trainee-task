package com.nostereal.avitotest.models

import com.google.gson.annotations.SerializedName

data class Pin(
    @SerializedName("id")
    val id: Long,
    @SerializedName("service")
    val service: String,
    @SerializedName("coordinates")
    val coordinates: Coordinates
)