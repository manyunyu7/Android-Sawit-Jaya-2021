package com.feylabs.sawitjaya.data.remote.response


import com.google.gson.annotations.SerializedName

data class UserInfoResponse(
    @SerializedName("contact")
    val contact: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("email_verified_at")
    val emailVerifiedAt: Any,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("photo")
    val photo: String,
    @SerializedName("photo_path")
    val photoPath: String,
    @SerializedName("role")
    val role: String,
    @SerializedName("status")
    val status: Any,
    @SerializedName("updated_at")
    val updatedAt: String
)