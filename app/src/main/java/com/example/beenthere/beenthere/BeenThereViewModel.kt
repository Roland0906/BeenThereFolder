package com.example.beenthere.beenthere

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beenthere.data.Experience
import com.example.beenthere.data.Situation
import com.example.beenthere.data.source.BeenThereRepository
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class BeenThereViewModel(private val repository: BeenThereRepository) : ViewModel() {

    private val db = Firebase.firestore
    private val collection = db.collection("situations")

    fun allSituations(): Flow<List<Situation>> = repository.getSituations()


    private val _navigateToAdvise = MutableLiveData<Situation>()
    val navigateToAdvise: LiveData<Situation>
        get() = _navigateToAdvise

    fun navigateToAdvise(situation: Situation) {
        _navigateToAdvise.value = situation
    }

    fun onAdviseNavigated() {
        _navigateToAdvise.value = null
    }

    init {
        setFirebaseListener()
    }

    private var snapshotListener: ListenerRegistration? = null
    private fun setFirebaseListener() {

        snapshotListener = collection.addSnapshotListener { snapShot, e ->
            if (e != null) {

                return@addSnapshotListener

            } else {
                if (snapShot != null && snapShot.size() != 0) {
                    if (snapShot.first().get("description") != null && snapShot.first()
                            .get("situation") != ""
                    ) {
                        for (document in snapShot) {
                            val situation = document.toObject<Situation>()
                            Log.i("BeenThere VM item from fireStore", situation.toString())
                            viewModelScope.launch {
                                // change to groovy to use python
                                // use open ai to convert python response to kotlin class
                                // check if already in Room before insert?

                                try {
                                    allSituations().collect {
                                        it.forEach {
                                            Log.i("Roland", "$it")
                                        }

                                        Log.i("Roland", "compare to $situation")

                                        if (!it.contains(situation)) {
                                            repository.upsertSituation(situation)
                                        }
                                    }

                                } catch (e: Exception) {
                                    Log.i("Insert FB data to room", e.message.toString())
                                }

                            }
                        }
                    }
                }
            }
        }
    }

    fun removeFirebaseListeners() {
        snapshotListener?.remove()
    }



}