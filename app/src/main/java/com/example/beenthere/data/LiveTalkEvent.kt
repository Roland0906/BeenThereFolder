package com.example.beenthere.data

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class LiveTalkEvent(

    @PrimaryKey(autoGenerate = true)
    @Json(name = "event_id") var eventId: Long = 0,

    @Json(name = "user_id") var userId: String = "",

    var topic: String = "",

    @Json(name = "is_going_on") var isGoingOn: Boolean = false,

    var avatar: String? = ""


) : Parcelable
