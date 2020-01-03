package com.nostereal.avitotest.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Coordinates(
    @SerializedName("lat")
    val latitude: Double,
    @SerializedName("lng")
    val longitude: Double
) : Parcelable