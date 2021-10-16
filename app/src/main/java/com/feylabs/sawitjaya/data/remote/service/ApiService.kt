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


interface ApiService {

    /*
    Note Belajar :
    - Suspend fun harus return datanya langsung
    - Call return enqueue, untuk callback
    - Resource untuk digunakan bareng RxJava, Coroutines (suspend)
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

    /**
     * register
     * @param get new profile
     * @return the token
     */
    @POST("auth/refresh")
    fun refreshToken(
        @Header("Authorization") authHeader: String?,
    ): Response<RefreshTokenResponse?>

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
     * rs
     * @param get request sell by id ( detailed )
     * @return
     */
    @GET("rs-chat/topic/{id}/get")
    suspend fun getRsChatByTopic(
        @Path("id") topicID: String?,
        @Query("hideDeleted") hideDeleted: Boolean = true,
        @Header("Authorization") authHeader: String?,
    ): Response<RsChatResponse?>

    /**
     * register
     * @param register
     * @return the token
     */
    @POST("rs-chat/store")
    fun storeChat(
        @Header("Authorization") token: String?,
        @Body body: RsChatStoreRequestBody?,
    ): Call<ResponseBody>

    /**
     * use to store new weight
     */
    @POST("rs-chat/store")
    fun storeRsScale(
        @Header("Authorization") token: String?,
        @Body body: RsChatStoreRequestBody?,
    ): Call<ResponseBody>

    /**
     * use to store new weight
     */
    @POST("request-sell/scale/all")
    @FormUrlEncoded
    fun getRsScaleAll(
        @Header("Authorization") token: String?,
        @Field("old_password") old_password: String,
    ): Call<ResponseBody>


    /**
     *
     * get request sell scaling by id
     */
    @GET("request-sell/{id}/scale/get")
    suspend fun getRsScaleByRsID(
        @Path("id") rsID: String?,
        @Header("Authorization") token: String?,
    ): Response<GetRsScaleByIDResponse>


    /**
     *
     * get request sell scaling by id
     */
    @POST("request-sell/{id}/scale/store?")
    @FormUrlEncoded
    suspend fun storeRsScaleByRsID(
        @Path("id") rsID: String?,
        @Field("result") result: String,
        @Field("created_by") createdBy: String,
        @Header("Authorization") token: String?,
    ): Response<OnlyMessageResponse>


    /**
     *
     * get request sell scaling by id
     */
    @POST("request-sell/scale/{id}/delete")
    suspend fun deleteRsScaleByID(
        @Path("id") rsID: String?,
        @Header("Authorization") token: String?,
    ): Response<OnlyMessageResponse>


    /**
     * get request sell scaling by id
     */
    @POST("request-sell/update-status")
    @FormUrlEncoded
    suspend fun changeRsStatus(
        @Field("id") rsID: String,
        @Field("status") status: String,
        @Header("Authorization") token: String?,
    ): Response<StandardAPIResponse>

    /**
     * get request sell scaling by id
     */
    @Multipart
    @POST("request-sell/{id}/store-signature")
    suspend fun storeRsSignature(
        @Path("id") rsID: String?,
        @Part("description") description: RequestBody?,
        @Part file: Part?,
        @Header("Authorization") token: String?,
        ): Response<StandardAPIResponse>


}