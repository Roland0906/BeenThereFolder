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


    private val _bookTitle = MutableLiveData<String>()
    val bookTitle: LiveData<String>
        get() = _bookTitle

    private val _bookAuthor = MutableLiveData<String>()
    val bookAuthor: LiveData<String>
        get() = _bookAuthor

    private val _bookImage = MutableLiveData<String>()
    val bookImage: LiveData<String>
        get() = _bookImage

    private val _myResponse: MutableLiveData<Books> = MutableLiveData()
    val myResponse: LiveData<Books> = _myResponse

    fun getBooks(title: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val response: Response<Books> = repository.getBooks(title, apiKey)
                if(response.isSuccessful && response.body() != null) {
                    _myResponse.value = response.body()
                } else {
                    throw java.lang.Exception("not getting book successfully or empty books")
                }
            } catch (e: Exception) {
                showError(e.message)
            }
        }
    }

    private val _upperText = MutableLiveData<String>()
    private val _lowerText = MutableLiveData<String>()
    val upperText: LiveData<String>
        get() = _upperText

    val lowerText: LiveData<String>
        get() = _lowerText

    fun getRecognizedLowerText(text: String) {
        _lowerText.value = text
    }

    fun getRecognizedUpperText(text: String) {
        _upperText.value = text
    }

//    var upperText = false
//    var lowerText = false

    fun getImage(image: String) {
        _bookImage.value = image
    }

    fun addData(
        userId: String,
        title: String,
        author: String,
        situation: String,
        phrases: String,
        image: String,
        isProcessed: Boolean
    ) {

        // to Room
        val exp = Experience(userId, title, author, situation, phrases, image, isProcessed)
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("Insert to Room", "success")
            try {
                repository.insertExp(exp)
            } catch (e: Exception) {
                Log.i("Share VM", "repeated PK? $e")
            }

        }

        // to FireStore
        val document = Firebase.firestore.collection("experiences").document()

        document.set(exp)
            .addOnSuccessListener {
//                showError("You have shared your experience")
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


