package com.nostereal.avitotest.models

import com.google.gson.annotations.SerializedName

data class PinsData(
    @SerializedName("services")
    val services: List<String>,
    @SerializedName("pins")
    val pins: List<Pin>
)