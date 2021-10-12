package com.feylabs.sawitjaya.data.remote.response


import com.google.gson.annotations.SerializedName

data class GetRsScaleByIDResponse(
    @SerializedName("api_code")
    val apiCode: Int,
    @SerializedName("http_response")
    val httpResponse: Int,
    @SerializedName("message_en")
    val messageEn: String,
    @SerializedName("message_id")
    val messageId: String,
    @SerializedName("res_data")
    val resData: ResData,
    @SerializedName("status_code")
    val statusCode: Int
)

data class ResData(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("total_weight")
    val totalWeight: Double
)

data class Data(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("created_by")
    val createdBy: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("result")
    val result: Int,
    @SerializedName("rs_id")
    val rsId: Int,
    @SerializedName("updated_at")
    val updatedAt: String
)