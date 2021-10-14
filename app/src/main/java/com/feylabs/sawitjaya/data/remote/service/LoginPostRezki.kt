package com.feylabs.sawitjaya.data.remote.service


import com.google.gson.annotations.SerializedName

data class LoginPostRezki(
    @SerializedName("phone")
    val phone: String
)