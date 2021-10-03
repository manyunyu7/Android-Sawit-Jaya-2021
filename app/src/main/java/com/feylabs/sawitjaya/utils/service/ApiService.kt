package com.feylabs.sawitjaya.utils.service

import com.feylabs.sawitjaya.data.remote.request.RegisterRequestBody
import com.feylabs.sawitjaya.data.remote.response.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

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

    @GET("auth/user-profile")
    suspend fun userInfo(): Response<UserInfoResponse>

    /**
     * register
     * @param get new profile
     * @return the token
     */
    @POST("auth/user-profile")
    fun getProfileByUser(
        @Header("Authorization") authHeader: String?,
    ): Response<UserInfoResponse?>

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
        @Header("Authorization") token: String?,
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


    /**
     * news
     * @param get request sell by user
     * @return
     */
    @GET("user/{id}/request-sell/?")
    suspend fun getRequestSellByUser(
        @Path("id") userID: String?,
        @Query("is_paginate") paginate: Boolean = true,
        @Query("page") page: Int = 1,
        @Query("per_page") per_page: Int = 10,
        @Query("status") status: String? = null,
        @Header("Authorization") authHeader: String?,
    ): Response<GetRequestSellByUserReq?>

    /**
     * rs
     * @param get request sell by id ( detailed )
     * @return
     */
    @GET("request-sell/{id}/detail")
    suspend fun getRequestSellDetail(
        @Path("id") userID: String?,
        @Header("Authorization") authHeader: String?,
    ): Response<HistoryDetailResponse?>

    /**
     * rs
     * @param get request sell by id ( detailed )
     * @return
     */
    @GET("mnotification/user/{id}")
    suspend fun getMNotificationByUser(
        @Path("id") userID: String?,
        @Header("Authorization") authHeader: String?,
    ): Response<MNotificationResponse?>

    /**
     * register
     * @param register
     * @return the token
     */
    @Headers("Content-Type: application/json")
    @POST("auth/login")
    suspend fun loginRezki(
        @Body body: LoginPostRezki,
    ): Response<LoginResponseRezki?>



}