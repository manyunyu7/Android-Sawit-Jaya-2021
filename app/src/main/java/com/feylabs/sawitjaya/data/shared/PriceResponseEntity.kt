package com.feylabs.sawitjaya.data.local.room.entity


import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "price")
data class PriceResponseEntity(
    @PrimaryKey
    @NonNull
    @SerializedName("id")
    @ColumnInfo(defaultValue = "")
    val id: Int,

    @ColumnInfo(defaultValue = "")
    @Nullable
    @SerializedName("created_at")
    val createdAt: String?,

    @ColumnInfo(defaultValue = "")
    @SerializedName("margin")
    val margin: Double?,

    @ColumnInfo(defaultValue = "")
    @SerializedName("price")
    val price: Double?,
    @SerializedName("updated_at")
    val updatedAt: String?
)
