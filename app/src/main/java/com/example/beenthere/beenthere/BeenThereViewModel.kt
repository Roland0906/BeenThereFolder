package com.example.beenthere.beenthere

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.beenthere.data.Experience
import com.example.beenthere.data.Situation
import com.example.beenthere.data.source.BeenThereRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow

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



}