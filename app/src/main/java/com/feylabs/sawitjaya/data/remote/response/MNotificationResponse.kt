package com.feylabs.sawitjaya.data.remote.response


import com.google.gson.annotations.SerializedName

class MNotificationResponse : ArrayList<MNotificationResponse.MNotificationResponseItem>(){
    data class MNotificationResponseItem(
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("desc")
        val desc: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("is_read")
        val isRead: Int,
        @SerializedName("message")
        val message: String,
        @SerializedName("rs_id")
        val rsId: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("type")
        val type: String,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("user_id")
        val userId: Int
    )
}