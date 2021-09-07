package com.feylabs.sawitjaya.data.remote.response


import com.google.gson.annotations.SerializedName

data class SellRequestResponse(
    @SerializedName("api_code")
    val apiCode: Int?,
    @SerializedName("http_response")
    val httpResponse: Int?,
    @SerializedName("message_en")
    val messageEn: String?,
    @SerializedName("message_id")
    val messageId: String?,
    @SerializedName("res_data")
    val resData: ResData?,
    @SerializedName("status_code")
    val statusCode: Int?
) {
    data class ResData(
        @SerializedName("SellRequest")
        val sellRequest: SellRequest?
    ) {
        data class SellRequest(
            @SerializedName("address")
            val address: String?,
            @SerializedName("contact")
            val contact: String?,
            @SerializedName("created_at")
            val createdAt: String?,
            @SerializedName("driver_id")
            val driverId: Any?,
            @SerializedName("est_margin")
            val estMargin: Double?,
            @SerializedName("est_price")
            val estPrice: Int?,
            @SerializedName("est_weight")
            val estWeight: String?,
            @SerializedName("id")
            val id: Int?,
            @SerializedName("lat")
            val lat: String?,
            @SerializedName("long")
            val long: String?,
            @SerializedName("staff_id")
            val staffId: Any?,
            @SerializedName("status")
            val status: String?,
            @SerializedName("updated_at")
            val updatedAt: String?,
            @SerializedName("user_id")
            val userId: Int?
        )
    }
}