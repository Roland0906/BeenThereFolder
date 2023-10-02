package com.example.beenthere.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize


@Entity(tableName = "situation_table")
@Parcelize
data class Situation(

    @PrimaryKey(autoGenerate = true)
    @Json(name = "situation_id") var situationId: Long = -1,
//    @Json(name = "situation_id") var situationId: Long = 0L,

    @ColumnInfo(name = "user_id")
    @Json(name = "user_id") var userId: String = "",

    @ColumnInfo(name = "description")
    var description: String? = "",

    @ColumnInfo(name = "is_processed")
    @Json(name = "is_processed") var isProcessed: Boolean = false

    ) : Parcelable {
    override fun equals(other: Any?): Boolean {

        other as Situation

        return this.userId == other.userId && this.description == other.description

    }
}

