package com.codepolitan.belajarngoding.model


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Content(
    @SerializedName("id_content")
    val idContent: Int? = null,
    @SerializedName("pages")
    val pages: List<Page>? = null
) : Parcelable