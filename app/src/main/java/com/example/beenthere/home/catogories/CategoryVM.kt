package com.example.beenthere.home.catogories

import android.content.Intent
import android.util.Log
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.beenthere.R
import com.example.beenthere.VideoActivity
import com.example.beenthere.data.ExpWithCount
import com.example.beenthere.data.Experience
import com.example.beenthere.data.LiveTalkEvent
import com.example.beenthere.data.source.BeenThereRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow

class CategoryVM (private val repository: BeenThereRepository,
// private val args: Experience
) : ViewModel() {

    private val db = Firebase.firestore
    fun allExp(): Flow<List<Experience>> = repository.getExp()



    private val _navigateToDetail = MutableLiveData<ExpWithCount>()

    val navigateToDetail: LiveData<ExpWithCount>
        get() = _navigateToDetail

    fun navigateToDetail(experience: ExpWithCount) {
        _navigateToDetail.value = experience
    }

    fun onDetailNavigated() {
        _navigateToDetail.value = null
    }


    private val commentDoc = db.collection("comments")

    private val commentCount = MutableLiveData<Int>()

//    private fun getComments() {
//        commentDoc.get()
//            .addOnSuccessListener { documents ->
//                documents.forEach {
//
//                }
//                commentCount.value = documents.size()
//                // Now, itemCount contains the number of documents in the "live_talks" collection
//                println("Number of items in 'live_talks': $commentCount")
//            }
//            .addOnFailureListener { exception ->
//                println("Error getting documents: $exception")
//            }
//    }
//
//    init {
//        getComments()
//    }






    fun launchLiveTalk(event: LiveTalkEvent): String {

        val docLive = db.collection("live_talks").document()

        event.eventId = docLive.id

        docLive.set(event)
            .addOnSuccessListener {

                Log.i("Category VM talk", "Launch success $event")
                showMessage("You're giving a live talk!")
            }
            .addOnFailureListener {
                showMessage("Launch live talk fail")
            }

        return docLive.id
    }

    val toastMessageLiveData = MutableLiveData<String?>()

    private fun showMessage(message: String?) {
        toastMessageLiveData.value = message
    }

}