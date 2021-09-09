package com.feylabs.sawitjaya.ui.user_history_tbs

import com.google.gson.annotations.SerializedName


data class HistoryPagingModel(
    val perPage: String,
    @SerializedName("current_page")
    val currentPage: Int,
    @SerializedName("data")
    val data : MutableList<HistoryModel>
){
    data class HistoryModel(
        val id: Int,
        val userId: Int,
        val address: String,
        val contact: String,
        val createdAt: String,
        val driverId: Int,
        val estMargin: String,
        val estPrice: String,
        val estWeight: String,
        val lat: String,
        val long: String,
        val userPhoto: String,
        val photo: String,
        val staffId: Int,
        val status: String,
        val statusDesc: String,
        val driverName: String,
        val userName: String,
        val staffName: String,
        val updatedAt: String,
        val updatedBy: Int,
    )
}
