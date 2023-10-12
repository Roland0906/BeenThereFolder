package com.example.beenthere.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.beenthere.data.Experience
import com.example.beenthere.data.source.BeenThereRepository
import com.example.beenthere.home.catogories.CategoryVM
import com.example.beenthere.home.catogories.detail.DetailVM


/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Factory for all ViewModels which need [Product].
 */
@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(
    private val beenThereRepository: BeenThereRepository,
    private val experience: Experience

) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(DetailVM::class.java) ->
                    DetailVM(beenThereRepository, experience)


                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
