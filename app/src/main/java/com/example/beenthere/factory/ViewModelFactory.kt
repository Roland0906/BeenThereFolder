package com.example.beenthere.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.beenthere.MainViewModel
import com.example.beenthere.beenthere.BeenThereViewModel

import com.example.beenthere.data.source.BeenThereRepository
import com.example.beenthere.home.HomeViewModel
import com.example.beenthere.home.catogories.CategoryVM
import com.example.beenthere.notalone.NotAloneViewModel
import com.example.beenthere.notalone.SuggestionViewModel
import com.example.beenthere.profile.ProfileViewModel
import com.example.beenthere.share.ShareViewModel


@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    private val beenThereRepository: BeenThereRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(MainViewModel::class.java) ->
                    MainViewModel(beenThereRepository)

                isAssignableFrom(HomeViewModel::class.java) ->
                    HomeViewModel(beenThereRepository)

                isAssignableFrom(ShareViewModel::class.java) ->
                    ShareViewModel(beenThereRepository)

                isAssignableFrom(CategoryVM::class.java) ->
                    CategoryVM(beenThereRepository)

                isAssignableFrom(NotAloneViewModel::class.java) ->
                    NotAloneViewModel(beenThereRepository)

                isAssignableFrom(BeenThereViewModel::class.java) ->
                    BeenThereViewModel(beenThereRepository)

                isAssignableFrom(SuggestionViewModel::class.java) ->
                    SuggestionViewModel(beenThereRepository)

                isAssignableFrom(ProfileViewModel::class.java) ->
                    ProfileViewModel(beenThereRepository)
//
//                isAssignableFrom(CheckoutSuccessViewModel::class.java) ->
//                    CheckoutSuccessViewModel(beenThereRepository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
