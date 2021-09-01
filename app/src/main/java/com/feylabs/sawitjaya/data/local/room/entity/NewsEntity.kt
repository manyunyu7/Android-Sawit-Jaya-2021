package com.feylabs.sawitjaya.data.local.room.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "news")
class NewsEntity(
    @PrimaryKey
    @NonNull
    var id: Int? = null,
    @ColumnInfo(defaultValue = "")
    var title: String? = null,
    @ColumnInfo(defaultValue = "")
    var author: String? = null,
    @ColumnInfo(defaultValue = "")
    var content: String? = null,
    @ColumnInfo(defaultValue = "")
    var photo: String? = null,
    @ColumnInfo(defaultValue = "")
    var created_at: String? = null,
    @ColumnInfo(defaultValue = "")
    var updated_at: String? = null,
)

