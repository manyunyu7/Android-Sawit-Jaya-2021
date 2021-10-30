package com.feylabs.sawitjaya.data.remote.response


import com.google.gson.annotations.SerializedName

data class LandingMessageResponse(
    @SerializedName("color")
    val color: String,
    @SerializedName("content_message")
    val contentMessage: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("photo")
    val photo: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("updated_at")
    val updatedAt: String
)