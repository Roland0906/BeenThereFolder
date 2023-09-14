package com.example.beenthere.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "situation_table")
@Parcelize
data class Situation(

    @PrimaryKey(autoGenerate = true)
    var sitId: Long = 0L,

    @ColumnInfo(name = "userId")
    val userId: Long = 0L,

    @ColumnInfo(name = "description")
    val description: String

    ) : Parcelable

