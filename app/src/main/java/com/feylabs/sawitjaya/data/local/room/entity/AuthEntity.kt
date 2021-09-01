package com.feylabs.sawitjaya.data.local.room.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "tb_user_base_info")
@Parcelize
class AuthEntity(
    @PrimaryKey
    @NonNull
    var user_id: Int? = null,
    @ColumnInfo(defaultValue = "")
    var name: String? = null,
    @ColumnInfo(defaultValue = "")
    var email: String? = null,
    @ColumnInfo(defaultValue = "")
    var contact: String? = null,
    @ColumnInfo(defaultValue = "")
    var photo: String? = null,
    @ColumnInfo(defaultValue = "")
    var role: String? = null,
    @ColumnInfo(defaultValue = "")
    var status: String? = "",
    @ColumnInfo(defaultValue = "")
    var photo_base64: String? = null,
) : Parcelable {

}


