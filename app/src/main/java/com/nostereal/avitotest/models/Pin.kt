package com.nostereal.avitotest.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pin(
    @SerializedName("id")
    val id: Long,
    @SerializedName("service")
    val service: String,
    @SerializedName("coordinates")
    val coordinates: Coordinates
) : Parcelable