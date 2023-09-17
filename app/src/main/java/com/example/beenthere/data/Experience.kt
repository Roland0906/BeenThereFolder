package com.example.beenthere.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "experience_table",
    primaryKeys = ["user_id", "book_title", "situation"],
//    foreignKeys = [ForeignKey(
//        entity = Book::class,
//        parentColumns = arrayOf("title"),
//        childColumns = arrayOf("book_title"),
//        onDelete = ForeignKey.CASCADE
//    )]
)
@Parcelize
data class Experience(

    @ColumnInfo(name = "user_id")
    val userId: String,

    @ColumnInfo(name = "book_title")
    val bookTitle: String,

    @ColumnInfo(name = "authors")
    val authors: String,

    @ColumnInfo(name = "situation")
    val situation: String,

    @ColumnInfo(name = "phrases")
    val phrases: String


) : Parcelable

