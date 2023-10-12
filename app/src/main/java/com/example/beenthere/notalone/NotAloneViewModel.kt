package com.example.beenthere.notalone

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beenthere.data.Situation
import com.example.beenthere.data.source.BeenThereRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotAloneViewModel(private val repository: BeenThereRepository) : ViewModel() {

    private val db = Firebase.firestore

    fun addData(situation: Situation) {

        // to FireStore
        val document = db.collection("situations").document()
        situation.situationId = document.id

        // to Room
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("Insert to Room", "success")
            repository.upsertSituation(situation)
        }

        document.set(situation)
            .addOnSuccessListener {
                Log.i("Insert to Firebase", "success")
            }
            .addOnFailureListener {
                showMessage("Something is wrong")
                Log.i("Insert to Firebase", it.message.toString())
            }
    }


    val toastMessageLiveData = MutableLiveData<String?>()

    private fun showMessage(message: String?) {
        toastMessageLiveData.value = message
    }

}