package com.feylabs.sawitjaya.ui.rs.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class GeoCoderModel(
    var address: String?,
    var city: String?,
    var state: String?,
    var country: String?,
    var postalCode: String?
) : Parcelable