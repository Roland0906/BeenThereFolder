package com.example.beenthere.share

import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beenthere.MainActivity
import com.example.beenthere.R
import com.example.beenthere.data.Experience
import com.example.beenthere.data.source.BeenThereRepository
import com.example.beenthere.model.Books
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import java.util.Calendar

class ShareViewModel(private val repository: BeenThereRepository) : ViewModel() {

    private val db = Firebase.firestore

    private val _bookTitle = MutableLiveData<String>()
    val bookTitle: LiveData<String>
        get() = _bookTitle

    private val _bookAuthor = MutableLiveData<String>()
    val bookAuthor: LiveData<String>
        get() = _bookAuthor

    private val _bookImage = MutableLiveData<String>()
    val bookImage: LiveData<String>
        get() = _bookImage

    val myResponse: MutableLiveData<Response<Books>> = MutableLiveData()

    fun getBooks(title: String, apiKey: String) {
        viewModelScope.launch {

            val response: Response<Books> = repository.getBooks(title, apiKey)
            myResponse.value = response
        }
    }

    fun getImage(image: String) {
        _bookImage.value = image
    }

    fun addData(userId: String, title: String, author: String, situation: String, phrases: String, image: String, isProcessed: Boolean) {

        // to Room
        val exp = Experience(userId, title, author, situation, phrases, image, isProcessed)
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("Insert to ROom", "success")
            repository.insertExp(exp)
        }

        // to FireStore
        val document = db.collection("experiences").document()

        document.set(exp)
            .addOnSuccessListener {
                showError("You have shared your experience")
                Log.i("Insert to Firestore", "success")
            }
            .addOnFailureListener {
                showError("Something is wrong")
            }
    }

    val toastMessageLiveData = MutableLiveData<String?>()

    private fun showError(message: String?) {
        toastMessageLiveData.value = message
    }


}