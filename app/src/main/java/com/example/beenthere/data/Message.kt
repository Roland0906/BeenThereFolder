package com.example.beenthere.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Message(
    var id : String = "",
    var message : String = "",
    var sentBy : String? = "",
    var timestamp : String = "",
    var isProcessed : Boolean = false,
    var avatar: String? = "",
    var expId: String? = ""
): Parcelable {
    companion object{
        const val SENT_BY_ME = "sent_me"
        const val SENT_BY_OTHERS = "sent_others"
    }
}
