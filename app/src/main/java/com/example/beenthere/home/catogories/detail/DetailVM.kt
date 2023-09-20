package com.example.beenthere.home.catogories.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.beenthere.data.Experience
import com.example.beenthere.data.source.BeenThereRepository

class DetailVM (private val repository: BeenThereRepository,
                private val args: Experience
) : ViewModel() {

    private val _exp = MutableLiveData<Experience>().apply {
        value = args
    }

    val exp: LiveData<Experience>
        get() = _exp


}