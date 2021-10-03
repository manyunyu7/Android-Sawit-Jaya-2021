package com.feylabs.sawitjaya.utils.service


import com.google.gson.annotations.SerializedName

data class LoginResponseRezki(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
) {
    data class Data(
        @SerializedName("access_token")
        val accessToken: String
    )
}