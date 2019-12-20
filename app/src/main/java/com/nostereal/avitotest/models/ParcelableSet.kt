package com.nostereal.avitotest.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ParcelableSet(val data: Set<String>) : Parcelable