package com.feylabs.sawitjaya.data.remote.service

import com.feylabs.sawitjaya.data.remote.request.RegisterRequestBody
import com.feylabs.sawitjaya.data.remote.request.RsChatStoreRequestBody
import com.feylabs.sawitjaya.data.remote.response.*
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody

import retrofit2.http.POST

import retrofit2.http.Multipart


interface AuthService {


    @FormUrlEncoded
    @POST("auth/login")
    fun login(
        @Field("email") username: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("auth/user-profile")
    suspend fun userInfo(): Response<UserInfoResponse>

    /**
     * register
     * @param register
     * @return the token
     */
    @POST("auth/register")
    fun register(
        @Header("Authorization") token: String?,
        @Body body: RegisterRequestBody?,
    ): Call<ResponseBody>?

}