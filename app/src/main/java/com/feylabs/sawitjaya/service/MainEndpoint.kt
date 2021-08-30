package com.feylabs.sawitjaya.service

import com.feylabs.sawitjaya.data.remote.response.LoginResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface MainEndpoint {

    /*
    Note Belajar :
    - Suspend fun harus return datanya langsung
    -
     */
    @GET("/test")
    suspend fun testAPI(): Response<String>


    @FormUrlEncoded
    @POST("auth/login")
     fun login(
        @Field("email") username: String,
        @Field("password") password: String
    ): Call<LoginResponse>
}