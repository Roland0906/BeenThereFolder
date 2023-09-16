package com.example.beenthere.data.source

import androidx.lifecycle.LiveData
import androidx.room.Insert
import com.example.beenthere.api.RetrofitInstance
import com.example.beenthere.data.Experience
import com.example.beenthere.model.Books
import retrofit2.Response


/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Interface to the Stylish layers.
 */
interface BeenThereRepository {

    suspend fun getBooks(title: String, apiKey: String): Response<Books> {
        return RetrofitInstance.api.getBooks(title, apiKey)
    }


    // db
    suspend fun insertExp(experience: Experience)

    fun getExpsFromRoom(): LiveData<List<Experience>>

    suspend fun clearRoom()

//    suspend fun getBooks(title: String, apiKey: String): Response<Books> {
//        return RetrofitInstance.api.getBooks(title, apiKey)
//    }

//    suspend fun getMarketingHots(): Result<List<HomeItem>>
//
//    suspend fun getProductList(type: String, paging: String? = null): Result<ProductListResult>
//
//    suspend fun getUserProfile(token: String): Result<User>
//
//    suspend fun userSignIn(fbToken: String): Result<UserSignInResult>
//
//    suspend fun userSignIn(email: String, password: String): Result<UserSignInResult>
//
//    suspend fun userSignUp(name: String, email: String, password: String): Result<UserSignUpResult>
//
//    suspend fun checkoutOrder(token: String, orderDetail: OrderDetail): Result<CheckoutOrderResult>
//
//    fun getProductsInCart(): LiveData<List<Book>>
//
//    suspend fun isProductInCart(id: Long, colorCode: String, size: String): Boolean
//
//    suspend fun insertProductInCart(product: Book)
//
//    suspend fun updateProductInCart(product: Book)
//
//    suspend fun removeProductInCart(id: Long, colorCode: String, size: String)
//
//    suspend fun clearProductInCart()
}
