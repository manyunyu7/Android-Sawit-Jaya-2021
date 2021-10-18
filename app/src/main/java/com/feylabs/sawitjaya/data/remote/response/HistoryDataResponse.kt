package com.feylabs.sawitjaya.data.remote.response


import com.google.gson.annotations.SerializedName

data class HistoryDataResponse(
    @SerializedName("current_page")
    val currentPage: Int,
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("first_page_url")
    val firstPageUrl: String,
    @SerializedName("from")
    val from: Int,
    @SerializedName("next_page_url")
    val nextPageUrl: Any,
    @SerializedName("path")
    val path: String,
    @SerializedName("per_page")
    val perPage: String,
    @SerializedName("prev_page_url")
    val prevPageUrl: String,
    @SerializedName("to")
    val to: Int
){
    data class Data(
        @SerializedName("address")
        val address: String,
        @SerializedName("contact")
        val contact: String,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("created_at_idn")
        val createdAtIdn: String,
        @SerializedName("driver_id")
        val driverId: Any,
        @SerializedName("driver_name")
        val driverName: String,
        @SerializedName("est_margin")
        val estMargin: String,
        @SerializedName("est_price")
        val estPrice: String,
        @SerializedName("est_weight")
        val estWeight: String,
        @SerializedName("final_margin")
        val finalMargin: Any,
        @SerializedName("final_price")
        val finalPrice: Any,
        @SerializedName("finished_at")
        val finishedAt: Any,
        @SerializedName("id")
        val id: Int,
        @SerializedName("lat")
        val lat: String,
        @SerializedName("long")
        val long: String,
        @SerializedName("margin_in_percentage")
        val marginInPercentage: Any,
        @SerializedName("photo")
        val photo: String,
        @SerializedName("photo_list")
        val photoList: List<Photo>,
        @SerializedName("photo_path")
        val photoPath: String,
        @SerializedName("photo_sign_driver")
        val photoSignDriver: Any,
        @SerializedName("photo_sign_owner")
        val photoSignOwner: Any,
        @SerializedName("photo_sign_staff")
        val photoSignStaff: Any,
        @SerializedName("price_paid")
        val pricePaid: Any,
        @SerializedName("real_calculation_price")
        val realCalculationPrice: Any,
        @SerializedName("result_est_price_now")
        val resultEstPriceNow: Any,
        @SerializedName("result_est_price_old")
        val resultEstPriceOld: Any,
        @SerializedName("rs_code")
        val rsCode: String,
        @SerializedName("signature_driver_path")
        val signatureDriverPath: String,
        @SerializedName("signature_staff_path")
        val signatureStaffPath: String,
        @SerializedName("signature_user_path")
        val signatureUserPath: String,
        @SerializedName("staff_id")
        val staffId: Any,
        @SerializedName("staff_name")
        val staffName: String,
        @SerializedName("status")
        val status: String,
        @SerializedName("status_desc")
        val statusDesc: String,
        @SerializedName("total_weight")
        val totalWeight: Any,
        @SerializedName("truck")
        val truck: Any,
        @SerializedName("truck_id")
        val truckId: Any,
        @SerializedName("truck_name")
        val truckName: String,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("updated_at_idn")
        val updatedAtIdn: String,
        @SerializedName("updated_by")
        val updatedBy: Any,
        @SerializedName("user_id")
        val userId: Int,
        @SerializedName("user_name")
        val userName: String,
        @SerializedName("user_photo")
        val userPhoto: String
    )
}