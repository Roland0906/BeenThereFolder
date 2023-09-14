package com.example.beenthere.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

///**
// * Created by Wayne Chen in Jul. 2019.
// */
@Entity(tableName = "books_table")
@Parcelize
data class Book(
    @PrimaryKey(autoGenerate = true)
    var randomId: Long = 0L,

    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "authors")
    val authors: String,
    @ColumnInfo(name = "image")
    val image: String,

) : Parcelable {
//    val stocks: Int
//        get() {
//            var value = 0
//            for (variant in variants) {
//                value += variant.stock
//            }
//            return value
//        }
//    @Embedded
//    var selectedVariant: Variant = Variant("", "", -1)
//    var amount: Long? = null
}
