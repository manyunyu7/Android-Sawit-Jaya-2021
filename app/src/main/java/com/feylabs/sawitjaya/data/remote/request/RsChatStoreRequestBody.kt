package com.feylabs.sawitjaya.data.remote.request


import com.google.gson.annotations.SerializedName
import java.io.File

data class RsChatStoreRequestBody(
    @SerializedName("message")
    val message: String,
    @SerializedName("photo")
    val photo: File? = null,
    @SerializedName("topic")
    val topic: String,
    @SerializedName("type")
    val type: String
)


