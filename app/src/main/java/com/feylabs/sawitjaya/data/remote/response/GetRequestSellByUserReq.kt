package com.feylabs.sawitjaya.data.remote.response


import com.google.gson.annotations.SerializedName

data class GetRequestSellByUserReq(
    @SerializedName("first_page_url")
    val firstPageUrl: String,
    @SerializedName("from")
    val from: Int,
    @SerializedName("next_page_url")
    val nextPageUrl: String,
    @SerializedName("path")
    val path: String,
    @SerializedName("per_page")
    val perPage: String,
    @SerializedName("current_page")
    val currentPage: Int,
    @SerializedName("data")
    val `data`: MutableList<DataRds>,
    @SerializedName("prev_page_url")
    val prevPageUrl: Int,
    @SerializedName("to")
    val to: Int
) {
    data class DataRds(
        @SerializedName("id")
        val id: Int,
        @SerializedName("user_id")
        val userId: Int,
        @SerializedName("address")
        val address: String,
        @SerializedName("contact")
        val contact: String,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("driver_id")
        val driverId: Int,
        @SerializedName("est_margin")
        val estMargin: String,
        @SerializedName("est_price")
        val estPrice: String,
        @SerializedName("est_weight")
        val estWeight: String,
        @SerializedName("lat")
        val lat: String,
        @SerializedName("long")
        val long: String,
        @SerializedName("user_photo")
        val userPhoto: String,
        @SerializedName("photo")
        val photo: String,
        @SerializedName("photo_path")
        val photoPath: String,
        @SerializedName("staff_id")
        val staffId: Int,
        @SerializedName("status")
        val status: String,
        @SerializedName("status_desc")
        val statusDesc: String,
        @SerializedName("driver_name")
        val driverName: String,
        @SerializedName("user_name")
        val userName: String,
        @SerializedName("staff_name")
        val staffName: String,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("updated_by")
        val updatedBy: Int,
    )
}