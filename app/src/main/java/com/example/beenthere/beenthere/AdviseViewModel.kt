package com.example.beenthere.beenthere

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.beenthere.data.Message
import com.example.beenthere.data.Situation
import com.example.beenthere.data.source.BeenThereRepository
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdviseViewModel ( private val repository: BeenThereRepository,
private val args: Situation
) : ViewModel() {

    private val _situation = MutableLiveData<Situation>().apply {
        value = args
    }

    val situation: LiveData<Situation>
        get() = _situation

    private val db = Firebase.firestore
    private val collection = db.collection("advices")

    private val _messageList = MutableLiveData<MutableList<Message>>()
    val messageList: LiveData<MutableList<Message>>
        get() = _messageList

    init {
        _messageList.value = mutableListOf()
    }

    private var snapshotListener: ListenerRegistration? = null
    fun setFireStoreListener() {

        snapshotListener = collection.orderBy("timestamp", Query.Direction.ASCENDING).addSnapshotListener { snapShot, e ->
            if (e != null) {

                return@addSnapshotListener

            } else {
                if (snapShot != null && snapShot.size() != 0) {
                    if (snapShot.first().get("message") != null && snapShot.first()
                            .get("message") != ""
                    ) {
                        val currentMessages = emptyList<Message>().toMutableList()
                        for (document in snapShot) {
                            val message = document.toObject<Message>()

                            if (message.expId == args.situationId.toString()) {
                                Log.i("DetailVM comment listened", message.toString())
                                currentMessages.add(Message(
                                    id = message.id,
                                    message = message.message,
                                    timestamp = message.timestamp
                                ))

                            }}
                        _messageList.value = currentMessages
                    }
                }
            }
        }
    }

    fun addAdvice(id: String, message: String) {

        val document = collection.document()

        // to Room
        val mensaje = Message(
            id = id,
            message = message,
            timestamp = getCurrentTimestamp(),
            isProcessed = false,
            expId = args.situationId.toString()
        )
        Log.i("DetailVM", mensaje.toString())

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
        return SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
    }



}
