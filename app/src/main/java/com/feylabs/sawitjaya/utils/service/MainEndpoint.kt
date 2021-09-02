package com.feylabs.sawitjaya.utils.service

import com.feylabs.sawitjaya.data.remote.request.RegisterRequestBody
import com.feylabs.sawitjaya.data.remote.response.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

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

    /**
     * register
     * @param get new profile
     * @return the token
     */
    @POST("auth/user-profile")
    fun getProfile(
        @Header("Authorization") authHeader: String?,
    ): Response<User>

    @FormUrlEncoded
    @POST("user/update-data")
    fun update_data(
        @Header("Authorization") token: String,
        @Field("user_email") email: String,
        @Field("user_role") role: String,
        @Field("user_name") name: String,
        @Field("user_contact") contact: String,
    ): Call<UserUpdateProfileResponse>


    /**
     * register
     * @param register
     * @return the token
     */
    @POST("auth/register")
    fun register(
        @Header("Authorization") authHeader: String?,
        @Body body: RegisterRequestBody?,
    ): Call<ResponseBody>?

    /**
     * register
     *
     * change password
     * @param token,old_password,new_password
     * @return the token
     */
    @FormUrlEncoded
    @POST("user/change-password")
    fun changePassword(
        @Header("Authorization") token: String?,
        @Field("old_password") old_password: String,
        @Field("new_password") new_password: String,
    ): Call<ChangePasswordResponse>?

    /**
     * news
     * @param get news data
     * @return the token
     */
    @GET("news/get")
    suspend fun getNews(
        @Header("Authorization") authHeader: String?,
    ): Response<NewsResponse?>

    /**
     * news
     * @param get news data
     * @return the token
     */
    @GET("price")
    suspend fun getPrice(
        @Header("Authorization") authHeader: String?,
    ): Response<PriceResponse?>



}