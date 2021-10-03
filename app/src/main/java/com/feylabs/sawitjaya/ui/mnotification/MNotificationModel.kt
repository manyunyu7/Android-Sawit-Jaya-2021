package com.feylabs.sawitjaya.ui.mnotification

data class MNotificationModel(
    val user_id: String = "",
    val rs_id: String? = "",
    val title: String = "",
    val desc: String = "",
    val message: String = "",
    val image: String = "",
    val type: String = "",
    val isRead: Int = 0,
    val createdAt: String = "",
)