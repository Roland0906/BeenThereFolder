package com.example.beenthere.home.catogories

import androidx.lifecycle.ViewModel
import com.example.beenthere.data.Experience
import com.example.beenthere.data.source.BeenThereRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow

class CategoryVM (private val repository: BeenThereRepository) : ViewModel() {

    fun allExp(): Flow<List<Experience>> = repository.getExp()

}