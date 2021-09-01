package com.feylabs.sawitjaya.data.local.room.entity


import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "price")
data class PriceResponseEntity(
    @PrimaryKey
    @NonNull

    @ColumnInfo(defaultValue = "")
    @SerializedName("created_at")
    val createdAt: String?,

    @ColumnInfo(defaultValue = "")
    @SerializedName("id")
    val id: Int?,

    @ColumnInfo(defaultValue = "")
    @SerializedName("margin")
    val margin: Double?,

    @ColumnInfo(defaultValue = "")
    @SerializedName("price")
    val price: Double?,
    @SerializedName("updated_at")
    val updatedAt: Any?
)
