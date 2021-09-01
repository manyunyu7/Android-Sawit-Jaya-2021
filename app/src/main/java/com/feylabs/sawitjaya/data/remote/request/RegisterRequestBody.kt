package com.feylabs.sawitjaya.data.remote.request


import com.google.gson.annotations.SerializedName

data class RegisterRequestBody(
    @SerializedName("user_contact")
    val userContact: String?,
    @SerializedName("user_email")
    val userEmail: String?,
    @SerializedName("user_name")
    val userName: String?,
    @SerializedName("user_password")
    val userPassword: String?,
    @SerializedName("user_role")
    val userRole: String?
)