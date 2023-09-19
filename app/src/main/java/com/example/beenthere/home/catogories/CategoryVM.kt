package com.example.beenthere.home.catogories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.beenthere.data.Experience
import com.example.beenthere.data.source.BeenThereRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow

class CategoryVM (private val repository: BeenThereRepository,
// private val args: Experience
) : ViewModel() {

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

}