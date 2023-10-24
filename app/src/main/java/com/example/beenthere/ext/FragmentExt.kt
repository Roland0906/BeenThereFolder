package com.example.beenthere.ext

import androidx.fragment.app.Fragment
import com.example.beenthere.BeenThereApplication
import com.example.beenthere.beenthere.AdviseViewModel
import com.example.beenthere.data.ExpWithCount
import com.example.beenthere.data.Experience
import com.example.beenthere.data.Situation
import com.example.beenthere.factory.AdviseViewModelFactory
import com.example.beenthere.factory.DetailViewModelFactory
import com.example.beenthere.factory.ViewModelFactory

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Extension functions for Fragment.
 */
fun Fragment.getVmFactory(): ViewModelFactory {

    val repository = (requireContext().applicationContext as BeenThereApplication).beenThereRepository
    return ViewModelFactory(repository)

}

//fun Fragment.getVmFactory(category: String): ViewModelFactory {
//
//    val repository = (requireContext().applicationContext as BeenThereApplication).beenThereRepository
//    return ViewModelFactory(repository)
//
//}


fun Fragment.getVmFactory(exp: ExpWithCount): DetailViewModelFactory {
    val repository = (requireContext().applicationContext as BeenThereApplication).beenThereRepository
    return DetailViewModelFactory(repository, exp)
}

fun Fragment.getVmFactory(situation: Situation): AdviseViewModelFactory {
    val repository = (requireContext().applicationContext as BeenThereApplication).beenThereRepository
    return AdviseViewModelFactory(repository, situation)
}





//fun Fragment.getVmFactory(user: User?): ProfileViewModelFactory {
//    val repository = (requireContext().applicationContext as BeenThereApplication).stylishRepository
//    return ProfileViewModelFactory(repository, user)
//}
//
//fun Fragment.getVmFactory(product: Book): DetailViewModelFactory {
//    val repository = (requireContext().applicationContext as BeenThereApplication).stylishRepository
//    return DetailViewModelFactory(repository, product)
//}
//
//fun Fragment.getVmFactory(catalogType: CatalogTypeFilter): CatalogItemViewModelFactory {
//    return CatalogItemViewModelFactory(catalogType)

