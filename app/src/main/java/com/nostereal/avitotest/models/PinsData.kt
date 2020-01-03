package com.nostereal.avitotest.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PinsData(
    @SerializedName("services")
    val services: List<String>,
    @SerializedName("pins")
    val pins: List<Pin>
) : Parcelable
