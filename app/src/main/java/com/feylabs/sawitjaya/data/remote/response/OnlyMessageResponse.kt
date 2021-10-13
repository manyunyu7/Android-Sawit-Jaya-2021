package com.feylabs.sawitjaya.data.remote.response


import com.google.gson.annotations.SerializedName

data class OnlyMessageResponse(
    @SerializedName("message")
    val messageString: String
)