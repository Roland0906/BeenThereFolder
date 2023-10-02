package com.example.beenthere

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.beenthere.data.Message
import com.example.beenthere.utils.UserManager.userId
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val collection = db.collection("messages")

    private val _messageList = MutableLiveData<MutableList<Message>>()
    val messageList: LiveData<MutableList<Message>>
        get() = _messageList

    init {
        _messageList.value = mutableListOf()
        _messageList.value?.clear()
    }

    fun clearMessageList() {

        _messageList.value?.clear()
    }


    private fun addToChat(id: String, message: String, sentBy: String, timestamp: String) {
        val currentList = _messageList.value ?: mutableListOf()
        currentList.add(Message(id, message, sentBy, timestamp))
        Log.i("WhatTheHell", currentList.toString())
        _messageList.postValue(currentList)
    }


    private fun addResponse(id: String, sentBy: String, response: String) {
//        if (_messageList.value?.size != 0) {
//            _messageList.value?.removeAt(_messageList.value?.size?.minus(1) ?: 0)
//        }
        addToChat(id, response, sentBy, getCurrentTimestamp())
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
                            if (!message.isProcessed) {
                                Log.i("Experience", message.toString())
                                val sentByWho = if (message.id == userId) {
                                    Message.SENT_BY_ME
                                } else {
                                    Message.SENT_BY_OTHERS
                                }
                                addResponse(message.id, sentByWho, message.message)
                                val docRef = collection.document(document.id)
                                val updatedData = message.copy(isProcessed = true)

                                docRef.set(updatedData)
                                    .addOnSuccessListener {
                                        Log.i("UpdateProcess", "Success")
                                    }
                                    .addOnFailureListener {
                                        Log.i("UpdateProcess", "Fail")
                                    }
                            }

                        }
                    }
                }
            }
        }
    }



    fun endLiveTalk(topic: String) {
        val docLiveTalk = db.collection("live_talks").document()

    }


    fun addData(id: String, message: String, sentBy: String, timestamp: String) {

        val document = collection.document()

        // to Room
        val mensaje = Message(id, message, sentBy, timestamp, false)

        // to FireStore

        document.set(mensaje)
            .addOnSuccessListener {
                showMessage("Message sent")
                Log.i("Insert to Firestore", "success")
            }
            .addOnFailureListener {
                showMessage("Something is wrong")
            }
    }

    val toastMessageLiveData = MutableLiveData<String?>()

    private fun showMessage(message: String?) {
        toastMessageLiveData.value = message
    }



    fun getCurrentTimestamp(): String {
        return SimpleDateFormat("hh mm a", Locale.getDefault()).format(Date())
    }

    fun removeFirebaseListener() {
        snapshotListener?.remove()
    }


}