package com.feylabs.sawitjaya.service

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Loading<T> : Resource<T>()
    class Success<T>(data: T,message: String="") : Resource<T>(data,message)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}