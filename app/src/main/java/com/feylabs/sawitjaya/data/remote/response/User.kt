package com.feylabs.sawitjaya.data.remote.response

data class User(
    val contact: String,
    val created_at: String,
    val email: String,
    val email_verified_at: Any,
    val id: Int,
    val name: String,
    val photo: String,
    val role: String,
    val status: Any,
    val updated_at: String
)