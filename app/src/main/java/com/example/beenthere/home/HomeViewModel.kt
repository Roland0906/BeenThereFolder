package com.example.beenthere.home

import androidx.lifecycle.ViewModel
import com.example.beenthere.data.source.BeenThereRepository

class HomeViewModel(private val beenThereRepository: BeenThereRepository) : ViewModel() {

    val text = "Test"

}