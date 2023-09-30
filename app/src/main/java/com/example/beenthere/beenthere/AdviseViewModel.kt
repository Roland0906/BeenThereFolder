package com.example.beenthere.beenthere

import androidx.lifecycle.ViewModel
import com.example.beenthere.data.Experience
import com.example.beenthere.data.Situation
import com.example.beenthere.data.source.BeenThereRepository

class AdviseViewModel ( private val repository: BeenThereRepository,
private val args: Situation
) : ViewModel() {

}
