package com.feylabs.sawitjaya.data.remote.response


import com.feylabs.sawitjaya.ui.rs.request.model.RsPhotoModel
import com.feylabs.sawitjaya.ui.user_history_tbs.detail.PhotoListModel
import com.google.gson.annotations.SerializedName

data class HistoryDetailResponse(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("driver_data")
    val driverData: User,
    @SerializedName("history_data")
    val historyData: List<HistoryData>,
    @SerializedName("price")
    val price: Price,
    @SerializedName("staff_data")
    val staffData: User,
    @SerializedName("staffs")
    val staffs: List<User>,
    @SerializedName("trucks")
    val trucks: List<Truck>,
    @SerializedName("user_data")
    val userData: UserData
) {
    data class Data(
        @SerializedName("address")
        val address: String,
        @SerializedName("contact")
        val contact: String,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("driver_id")
        val driverId: Int,
        @SerializedName("driver_name")
        val driverName: String,
        @SerializedName("est_margin")
        val estMargin: String,
        @SerializedName("est_price")
        val estPrice: String,
        @SerializedName("est_weight")
        val estWeight: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("lat")
        val lat: String,
        @SerializedName("long")
        val long: String,
        @SerializedName("photo")
        val photo: String,
        @SerializedName("photo_list")
        val photoList: List<Photo>,
        @SerializedName("photo_path")
        val photoPath: String,
        @SerializedName("staff_id")
        val staffId: Int,
        @SerializedName("staff_name")
        val staffName: String,
        @SerializedName("status")
        val status: String,
        @SerializedName("status_desc")
        val statusDesc: String,
        @SerializedName("truck")
        val truck: Truck,
        @SerializedName("truck_id")
        val truckId: Int,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("updated_by")
        val updatedBy: Any,
        @SerializedName("user_id")
        val userId: Int,
        @SerializedName("user_name")
        val userName: String,
        @SerializedName("user_photo")
        val userPhoto: String
    ) {
        fun mapToPhotoModel(): MutableList<PhotoListModel> {
            val tempList = mutableListOf<PhotoListModel>()
            photoList.forEach {
                tempList.add(PhotoListModel(it.photoPath))
            }
            return tempList
        }

        data class Photo(
            @SerializedName("created_at")
            val createdAt: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("path")
            val path: String,
            @SerializedName("photo_path")
            val photoPath: String,
            @SerializedName("request_sell_id")
            val requestSellId: Int,
            @SerializedName("updated_at")
            val updatedAt: String
        ) {

        }

        data class Truck(
            @SerializedName("created_at")
            val createdAt: String,
            @SerializedName("created_by")
            val createdBy: Int,
            @SerializedName("deleted_at")
            val deletedAt: Any,
            @SerializedName("deleted_by")
            val deletedBy: Any,
            @SerializedName("description")
            val description: Any,
            @SerializedName("fuel_type")
            val fuelType: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("image_full_path")
            val imageFullPath: String,
            @SerializedName("name")
            val name: String,
            @SerializedName("nopol")
            val nopol: String,
            @SerializedName("photo")
            val photo: String,
            @SerializedName("updated_at")
            val updatedAt: String
        )
    }


    data class HistoryData(
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("desc")
        val desc: String,
        @SerializedName("driver")
        val driver: User,
        @SerializedName("id")
        val id: Int,
        @SerializedName("id_driver")
        val idDriver: Int,
        @SerializedName("id_rs")
        val idRs: Int,
        @SerializedName("id_staff")
        val idStaff: Int,
        @SerializedName("id_truck")
        val idTruck: Int,
        @SerializedName("photo")
        val photo: Any,
        @SerializedName("staff")
        val staff: User,
        @SerializedName("status")
        val status: String,
        @SerializedName("status_desc")
        val statusDesc: String,
        @SerializedName("truck")
        val truck: Truck,
        @SerializedName("updated_at")
        val updatedAt: String
    ) {


        data class Truck(
            @SerializedName("contact")
            val contact: String,
            @SerializedName("created_at")
            val createdAt: String,
            @SerializedName("email")
            val email: String,
            @SerializedName("email_verified_at")
            val emailVerifiedAt: Any,
            @SerializedName("id")
            val id: Int,
            @SerializedName("name")
            val name: String,
            @SerializedName("photo")
            val photo: String,
            @SerializedName("photo_path")
            val photoPath: String,
            @SerializedName("role")
            val role: String,
            @SerializedName("status")
            val status: Any,
            @SerializedName("updated_at")
            val updatedAt: String
        )
    }

    data class Price(
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("margin")
        val margin: Double,
        @SerializedName("price")
        val price: Double,
        @SerializedName("updated_at")
        val updatedAt: Any
    )


    data class Truck(
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("created_by")
        val createdBy: Int,
        @SerializedName("deleted_at")
        val deletedAt: Any,
        @SerializedName("deleted_by")
        val deletedBy: Any,
        @SerializedName("description")
        val description: Any,
        @SerializedName("fuel_type")
        val fuelType: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("image_full_path")
        val imageFullPath: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("nopol")
        val nopol: String,
        @SerializedName("photo")
        val photo: String,
        @SerializedName("updated_at")
        val updatedAt: String
    )


}

data class UserData(
    @SerializedName("contact")
    val contact: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("email_verified_at")
    val emailVerifiedAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("photo")
    val photo: String,
    @SerializedName("photo_path")
    val photoPath: String,
    @SerializedName("role")
    val role: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("updated_at")
    val updatedAt: String
)