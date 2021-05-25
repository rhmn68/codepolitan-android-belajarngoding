package com.codepolitan.belajarngoding.model


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Page(
    @SerializedName("parts_page")
    val partsPage: List<PartsPage>? = null
) : Parcelable