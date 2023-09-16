package com.example.beenthere.home

import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beenthere.MainActivity
import com.example.beenthere.data.Experience
import com.example.beenthere.data.openai.ApiClient
import com.example.beenthere.data.source.BeenThereRepository
import com.example.beenthere.model.Books
import com.example.beenthere.model.openai.CompletionRequest
import com.example.beenthere.model.openai.CompletionResponse
import com.example.beenthere.model.openai.Message
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Response
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeViewModel(private val repository: BeenThereRepository) : ViewModel() {


    private val _messageList = MutableLiveData<MutableList<Message>>()
    val messageList: LiveData<MutableList<Message>>
        get() = _messageList

    init {
        _messageList.value = mutableListOf()
    }


    val allExps = repository.getExpsFromRoom()

    fun analyzer() {

        Log.i("Response1 allExps", allExps.value.toString())
        allExps.value?.forEach {
            callApi(it.situation)
        }
    }


    fun addToChat(message: String, sentBy: String, timestamp: String) {
        val currentList = _messageList.value ?: mutableListOf()
        currentList.add(Message(message, sentBy, timestamp))
        _messageList.postValue(currentList)
    }

    fun addResponse(response: String) {
        _messageList.value?.removeAt(_messageList.value?.size?.minus(1) ?: 0)
        addToChat(response, Message.SENT_BY_BOT, getCurrentTimestamp())
    }

    enum class CATEGORY {
        MEANING,
        COMMUNICATION,
        DISCIPLINE,
        LEARNING,
        WORK,
        RELATIONSHIP
    }

    val categories =
        CATEGORY.entries // Log(toString()) : [MEANING, COMMUNICATION, DISCIPLINE, LEARNING, WORK, RELATIONSHIP]

    private val checkCategoryString =
        "Which one of the 6 types $categories does the following description belong to:"

    fun callApi(question: String) {
        addToChat("Typing...", Message.SENT_BY_BOT, getCurrentTimestamp())

        Log.i("Response2", "callApi")
        val completionRequest = CompletionRequest(
            model = "text-davinci-003",
            prompt = checkCategoryString + question,
            max_tokens = 4000
        )

        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getCompletions(completionRequest)
                Log.i("Response3 response", response.toString())
                handleApiResponse(response)
            } catch (e: SocketTimeoutException) {
                addResponse("Timeout: $e")
            }
        }
    }

    private suspend fun handleApiResponse(response: Response<CompletionResponse>) {
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                response.body()?.let { completionResponse ->
                    val result = completionResponse.choices.firstOrNull()?.text
                    if (result != null) {
//                        categorize(result.trim())
                        Log.i("Response4 result", result.trim())
//                        addResponse(result.trim())
                    } else {
                        addResponse("No choices found")
                    }
                }
            } else {
                addResponse("Failed to get response ${response.errorBody()}")
            }
        }
    }

    private fun categorize(result: String) {

    }


    fun getCurrentTimestamp(): String {
        return SimpleDateFormat("hh mm a", Locale.getDefault()).format(Date())
    }


}