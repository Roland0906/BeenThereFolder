package com.example.beenthere.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize


@Entity(tableName = "experience_table",
    primaryKeys = ["user_id", "title", "situation"],
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
    @Json(name = "user_id") var userId: String = "",

    @ColumnInfo(name = "title")
    var title: String = "",

    @ColumnInfo(name = "author")
    var author: String? = "",

    @ColumnInfo(name = "situation")
    var situation: String = "",

    @ColumnInfo(name = "phrases")
    var phrases: String? = "",

    @ColumnInfo(name = "image")
    var image: String? = "",

    @ColumnInfo(name = "is_processed")
    @Json(name = "is_processed") var isProcessed: Boolean = false,

    @ColumnInfo(name = "category")
    var category: String? = "",




//    @ColumnInfo(name = "thread")
//    var thread: List<Message>?


) : Parcelable {
    override fun equals(other: Any?): Boolean {

        other as Experience

        return (this.situation == other.situation && this.userId == other.userId && this.title == other.title)

//        return super.equals(other)
    }
}

fun Experience.toExpWithCount() =
    ExpWithCount(
        exp = this,
        count = 0
    )


data class ExpWithCount(

    val exp: Experience,
    var count: Int

)




