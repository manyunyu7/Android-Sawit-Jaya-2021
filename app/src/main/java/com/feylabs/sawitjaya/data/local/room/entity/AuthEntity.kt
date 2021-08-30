package com.feylabs.sawitjaya.data.local.room.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "tb_user_base_info")
@Parcelize
class AuthEntity(
    @PrimaryKey
    @NonNull
    var user_id: Int? = null,
    @Nullable
    var name: String? = null,
    @Nullable
    var email: String? = null,
    @Nullable
    var contact: String? = null,
    @Nullable
    var photo: String? = null,
    var role: String? = null,
    var status: String? = null,
    var photo_base64: String? = null,
) : Parcelable {

}


