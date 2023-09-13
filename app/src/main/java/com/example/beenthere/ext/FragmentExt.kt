package com.example.beenthere.ext

import androidx.fragment.app.Fragment
import com.example.beenthere.BeenThereApplication
import com.example.beenthere.factory.ViewModelFactory

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Extension functions for Fragment.
 */
fun Fragment.getVmFactory(): ViewModelFactory {

    val repository = (requireContext().applicationContext as BeenThereApplication).stylishRepository
    return ViewModelFactory(repository)
}

//fun Fragment.getVmFactory(user: User?): ProfileViewModelFactory {
//    val repository = (requireContext().applicationContext as BeenThereApplication).stylishRepository
//    return ProfileViewModelFactory(repository, user)
//}
//
//fun Fragment.getVmFactory(product: Product): ProductViewModelFactory {
//    val repository = (requireContext().applicationContext as BeenThereApplication).stylishRepository
//    return ProductViewModelFactory(repository, product)
//}
//
//fun Fragment.getVmFactory(catalogType: CatalogTypeFilter): CatalogItemViewModelFactory {
//    return CatalogItemViewModelFactory(catalogType)

