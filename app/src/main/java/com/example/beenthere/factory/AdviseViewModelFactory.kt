package com.example.beenthere.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.beenthere.beenthere.AdviseViewModel
import com.example.beenthere.data.Experience
import com.example.beenthere.data.Situation
import com.example.beenthere.data.source.BeenThereRepository
import com.example.beenthere.home.catogories.detail.DetailVM

@Suppress("UNCHECKED_CAST")
class AdviseViewModelFactory(
    private val beenThereRepository: BeenThereRepository,
    private val situation: Situation

) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(AdviseViewModel::class.java) ->
                    AdviseViewModel(beenThereRepository, situation)


                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}