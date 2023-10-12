package com.example.beenthere.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize


@Parcelize
data class User (
    @PrimaryKey(autoGenerate = false)
    @Json(name = "user_id") var userId: String = "",

    @Json(name = "user_name") var userName: String = "",

    @ColumnInfo(name = "user_avatar")
    @Json(name = "user_avatar") var userAvatar: String? = ""
)  : Parcelable