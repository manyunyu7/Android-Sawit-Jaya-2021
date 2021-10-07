package com.feylabs.sawitjaya.data.remote.response


import com.google.gson.annotations.SerializedName

class RsChatResponse : ArrayList<RsChatResponseItem>()

data class RsChatResponseItem(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("id_sender")
    val idSender: Int? = 0,
    @SerializedName("is_deleted")
    val isDeleted: Any,
    @SerializedName("is_read")
    val isRead: Any,
    @SerializedName("is_send")
    val isSend: Any,
    @SerializedName("message")
    val message: String,
    @SerializedName("sender_name")
    val senderName: String,
    @SerializedName("sender_photo")
    val senderPhoto: String,
    @SerializedName("topic")
    val topic: Int,
    @SerializedName("type")
    val type: Any,
    @SerializedName("updated_at")
    val updatedAt: String
)