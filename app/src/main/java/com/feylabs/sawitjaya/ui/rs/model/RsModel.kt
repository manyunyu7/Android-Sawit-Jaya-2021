package com.feylabs.sawitjaya.ui.rs.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class RsModel(
    var address: String?,
    var contact: String? = "",
    var lat: String? = "",
    var long: String? = "",
    var est_price: String? = "",
    var est_weight: String? = "",
    var photos: MutableList<File>? = null
) : Parcelable