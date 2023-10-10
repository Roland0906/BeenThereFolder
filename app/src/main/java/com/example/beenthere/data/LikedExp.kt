package com.example.beenthere.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class LikedExp(
    @PrimaryKey(autoGenerate = false)
    @Json(name = "liked_exp_id") var likeExpId: String = "",

    @Json(name = "user_id") var userId: String = "",

    @ColumnInfo(name = "experience")
    @Json(name = "experience") var experience: Experience = Experience(),

    @ColumnInfo(name = "is_Liked")
    @Json(name = "is_Liked") var isLiked: Boolean = false

) : Parcelable
