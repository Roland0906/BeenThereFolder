package com.example.beenthere.home.catogories.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.beenthere.data.Experience
import com.example.beenthere.data.Message
import com.example.beenthere.data.source.BeenThereRepository
import com.example.beenthere.utils.UserManager
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailVM(
    private val repository: BeenThereRepository,
    private val args: Experience
) : ViewModel() {

    private val _exp = MutableLiveData<Experience>().apply {
        value = args
    }

    val exp: LiveData<Experience>
        get() = _exp

    private val db = Firebase.firestore
    private val collection = db.collection("comments")

    private val _messageList = MutableLiveData<MutableList<Message>>()
    val messageList: LiveData<MutableList<Message>>
        get() = _messageList

    init {
        _messageList.value = mutableListOf()
    }

    private fun addCommentToList(id: String, message: String, timestamp: String) {
        val currentList = _messageList.value ?: mutableListOf()
        currentList.add(Message(id = id, message = message, timestamp = timestamp))
        Log.i("DetailVM comment list", currentList.toString())
        _messageList.postValue(currentList)
    }

    private var snapshotListener: ListenerRegistration? = null
    fun setFireStoreListener() {

        snapshotListener = collection.addSnapshotListener { snapShot, e ->
            if (e != null) {

                return@addSnapshotListener

            } else {
                if (snapShot != null && snapShot.size() != 0) {
                    if (snapShot.first().get("message") != null && snapShot.first()
                            .get("message") != ""
                    ) {
                        for (document in snapShot) {
                            val message = document.toObject<Message>()
//                            if (!message.isProcessed) {
                            Log.i("DetailVM comment listened", message.toString())
                            addCommentToList(
                                id = message.id,
                                message = message.message,
                                timestamp = getCurrentTimestamp()
                            )
//                                val docRef = collection.document(document.id)
//                                val updatedData = message.copy(isProcessed = true)
//
//                                docRef.set(updatedData)
//                                    .addOnSuccessListener {
//                                        Log.i("UpdateProcess", "Success")
//                                    }
//                                    .addOnFailureListener {
//                                        Log.i("UpdateProcess", "Fail")
//                                    }
//                            }

                        }
                    }
                }
            }
        }
    }

    fun addComment(id: String, message: String) {

        val document = collection.document()

        // to Room
        val mensaje = Message(
            id = id,
            message = message,
            timestamp = getCurrentTimestamp(),
            isProcessed = false
        )

        // to FireStore

        document.set(mensaje)
            .addOnSuccessListener {
                showMessage("Message sent")
                Log.i("DetailVM Insert to fb", "success")
            }
            .addOnFailureListener {
                showMessage("Something is wrong")
            }
    }

    val toastMessageLiveData = MutableLiveData<String?>()

    private fun showMessage(message: String?) {
        toastMessageLiveData.value = message
    }


    private fun getCurrentTimestamp(): String {
        return SimpleDateFormat("hh mm a", Locale.getDefault()).format(Date())
    }


}