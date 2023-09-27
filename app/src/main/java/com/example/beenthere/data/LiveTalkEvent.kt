package com.example.beenthere.data

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class LiveTalkEvent(

    @PrimaryKey(autoGenerate = true)
    @Json(name = "event_id") var eventId: Long = 0L,

    @Json(name = "user_id") var userId: String = "",

    var theme: String = "",




) : Parcelable
