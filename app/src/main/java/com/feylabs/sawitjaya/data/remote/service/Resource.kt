package com.feylabs.sawitjaya.data.remote.service

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Success<T>(data: T, message: String = "") : Resource<T>(data, message)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Default<T>(data: T? = null) : Resource<T>(data)
}