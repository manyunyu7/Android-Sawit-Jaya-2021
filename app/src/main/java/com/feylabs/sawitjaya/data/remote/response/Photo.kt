package com.feylabs.sawitjaya.data.remote.response


import com.google.gson.annotations.SerializedName

data class Photo(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("path")
    val path: String,
    @SerializedName("photo_path")
    val photoPath: String,
    @SerializedName("request_sell_id")
    val requestSellId: Int,
    @SerializedName("updated_at")
    val updatedAt: String
)