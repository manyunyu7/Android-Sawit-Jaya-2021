package com.feylabs.sawitjaya.data.remote.response

data class LoginResponse(
    val access_token: String,
    val expires_in: Int,
    val message: String,
    val status_code: Int,
    val token_type: String,
    val user: User
)