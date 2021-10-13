package com.feylabs.sawitjaya.ui.user_history_tbs.scale

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RsScaleModel(
    val id: String,
    val result: Double,
    val inputedBy: String,
    val inputedAt: String,
) : Parcelable