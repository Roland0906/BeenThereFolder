package com.example.beenthere.home.catogories

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.beenthere.R
import com.example.beenthere.VideoActivity
import com.example.beenthere.data.Experience
import com.example.beenthere.data.LiveTalkEvent
import com.example.beenthere.data.source.BeenThereRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow

class CategoryVM (private val repository: BeenThereRepository,
// private val args: Experience
) : ViewModel() {

    private val db = Firebase.firestore
    fun allExp(): Flow<List<Experience>> = repository.getExp()



    private val _navigateToDetail = MutableLiveData<Experience>()

    val navigateToDetail: LiveData<Experience>
        get() = _navigateToDetail

    fun navigateToDetail(experience: Experience) {
        _navigateToDetail.value = experience
    }

    fun onDetailNavigated() {
        _navigateToDetail.value = null
    }

    private val userList =
        listOf("PollyYana", "Jammy", "Jayson", "Terry", "Elephant", "Timothy", "Bryant", "Jackson")


    fun launchLiveTalk(topic: String) {

        val docLive = db.collection("live_talks").document()

        val event = LiveTalkEvent(userId = userList.random(), topic = topic, isGoingOn = true)

        docLive.set(event)
            .addOnSuccessListener {
                Log.i("HomeVM live talk", "Launch success")
                showMessage("You're giving a live talk!")
            }
            .addOnFailureListener {
                showMessage("Launch live talk fail")
            }
    }

    val toastMessageLiveData = MutableLiveData<String?>()

    private fun showMessage(message: String?) {
        toastMessageLiveData.value = message
    }

}