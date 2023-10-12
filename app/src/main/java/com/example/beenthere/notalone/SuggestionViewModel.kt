package com.example.beenthere.notalone

import androidx.lifecycle.ViewModel
import com.example.beenthere.data.Experience
import com.example.beenthere.data.Situation
import com.example.beenthere.data.source.BeenThereRepository
import kotlinx.coroutines.flow.Flow

class SuggestionViewModel(private val repository: BeenThereRepository) : ViewModel() {

    fun allExp(): Flow<List<Experience>> = repository.getExp()

}