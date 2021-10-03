package com.feylabs.sawitjaya.utils.service


import com.google.gson.annotations.SerializedName

data class LoginPostRezki(
    @SerializedName("phone")
    val phone: String
)