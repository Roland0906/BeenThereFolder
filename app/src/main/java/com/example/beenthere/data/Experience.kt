package com.example.beenthere.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "experience_table")
@Parcelize
data class Experience(

    @PrimaryKey(autoGenerate = true)
    var expId: Long = 0L,

    @ColumnInfo(name = "userId")
    val userId: Long = 0L,

    @ColumnInfo(name = "bookTitle")
    val bookTitle: String,

    @ColumnInfo(name = "situation")
    val situation: String,

    @ColumnInfo(name = "description")
    val description: String


) : Parcelable
