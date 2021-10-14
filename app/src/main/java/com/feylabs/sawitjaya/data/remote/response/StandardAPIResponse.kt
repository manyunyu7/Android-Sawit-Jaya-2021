package com.feylabs.sawitjaya.data.remote.response


import com.google.gson.annotations.SerializedName

data class StandardAPIResponse(
    @SerializedName("api_code")
    val apiCode: Int,
    @SerializedName("http_response")
    val httpResponse: Int,
    @SerializedName("message_en")
    val messageEn: String,
    @SerializedName("message_id")
    val messageId: String,
    @SerializedName("res_data")
    val resData: Any,
    @SerializedName("status_code")
    val statusCode: Int
)