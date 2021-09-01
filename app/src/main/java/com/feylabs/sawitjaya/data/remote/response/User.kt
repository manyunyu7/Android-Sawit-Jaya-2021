package com.feylabs.sawitjaya.data.remote.response

data class User(
    val id: Int,
    val contact: String,
    val created_at: String,
    val email: String,
    val email_verified_at: String = "",
    val name: String,
    val photo: String,
    val photo_path: String = "",
    val role: String,
    val status: String = "",
    val updated_at: String
)