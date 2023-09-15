package com.example.beenthere.share

import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beenthere.MainActivity
import com.example.beenthere.data.source.BeenThereRepository
import com.example.beenthere.model.Books
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response

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



}