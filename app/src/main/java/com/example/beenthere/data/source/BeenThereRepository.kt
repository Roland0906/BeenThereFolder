package com.example.beenthere.data.source

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.beenthere.api.RetrofitInstance
import com.example.beenthere.data.Experience
import com.example.beenthere.data.Situation
import com.example.beenthere.model.Books
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
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

    suspend fun upsertSituation(situation: Situation)

    suspend fun updateExp(experience: Experience)

    suspend fun insertManyExp(experiences: List<Experience>)

//    fun getExp(): LiveData<List<Experience>>

    fun getExp(): Flow<List<Experience>>

    fun getSituations(): Flow<List<Situation>>

    suspend fun clearExpInRoom()



//    fun setFirebaseListener(db: FirebaseFirestore, collection: String): Flow<List<Experience>> = callbackFlow {
//        val listenerRegistration = db.collection(collection).addSnapshotListener { snapshot, e ->
//            if (e != null) {
//                Log.i("Set listener", "fail: $e")
//                close(e)
//                return@addSnapshotListener
//            }
//
//            val shares = snapshot?.documents?.mapNotNull { document ->
//                document.toObject<Experience>()
//            } ?: emptyList()
//
//
//            trySend(shares)
//        }
//
//        awaitClose { listenerRegistration.remove() }
//
//    }

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
