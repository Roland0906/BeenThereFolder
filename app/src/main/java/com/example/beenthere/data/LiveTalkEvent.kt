package com.example.beenthere.data

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class LiveTalkEvent(

    @PrimaryKey(autoGenerate = false)
    @Json(name = "event_id") var eventId: String = "",

    @Json(name = "user_id") var userId: String = "",

    var topic: String = "",

    @Json(name = "going_on") var goingOn: Boolean = false,

    var avatar: String? = ""


) : Parcelable
