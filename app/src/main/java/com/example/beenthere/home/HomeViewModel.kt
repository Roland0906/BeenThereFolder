package com.example.beenthere.home

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

class HomeViewModel(private val repository: BeenThereRepository) : ViewModel() {


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

    init {
        _bookImage.value = "http://books.google.com/books/content?id=u8w_DwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"
    }

    fun getImage(image: String) {
        _bookImage.value = image
    }

    fun searchBook(inputQuery: String) {
        val author = ""
        val key = "AIzaSyDrfi3kT0VWATXU50FhWs7g0LBBgq-aFhw"
        val client = AsyncHttpClient()
        val url = "https://www.googleapis.com/books/v1/volumes?q=${inputQuery}:&key=${key}" // &key=${key}
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                Log.d(TAG, result)

                try {
                    val jsonObject = JSONObject(result)
                    val itemsArray = jsonObject.getJSONArray("items")

                    var i = 0

                    while (i < itemsArray.length()) {

                        val book = itemsArray.getJSONObject(i)
                        val volumeInfo = book.getJSONObject("volumeInfo")

                        try {

                            _bookTitle.value = volumeInfo.getString("title")
                            _bookAuthor.value = volumeInfo.getString("authors")
                            var thumbnailUrl = ""
                            val thumbNailObject = volumeInfo.optJSONObject("imageLinks")
                            if (thumbNailObject != null && thumbNailObject.has("smallThumbnail")) {
                                thumbnailUrl = thumbNailObject.getString("smallThumbnail")
                            }
                            _bookImage.value = thumbnailUrl

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        i ++
                    }


                } catch (e: Exception) {
                    showError(e.message)
                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "Status code: Bad request"
                    403 -> "Status code: Forbidden"
                    404 -> "Status code: Not found"
                    else -> "Status code: ${error?.message}"
                }
                showError(errorMessage)
            }

        })
    }

    val toastMessageLiveData = MutableLiveData<String?>()

    private fun showError(message: String?) {
        toastMessageLiveData.value = message
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

}