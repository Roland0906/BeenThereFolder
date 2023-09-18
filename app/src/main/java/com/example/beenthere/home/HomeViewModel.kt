package com.example.beenthere.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beenthere.data.Experience
import com.example.beenthere.data.openai.ApiClient
import com.example.beenthere.data.source.BeenThereRepository
import com.example.beenthere.model.openai.CompletionRequest
import com.example.beenthere.model.openai.CompletionResponse
import com.example.beenthere.model.openai.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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


    val allExp = repository.getExp()

    private val processedExp: MutableList<Experience> = mutableListOf()

    var workList = mutableListOf<Experience>()
    var meaningList = mutableListOf<Experience>()
    var communicationList = mutableListOf<Experience>()
    var disciplineList = mutableListOf<Experience>()
    var learningList = mutableListOf<Experience>()
    var relationshipList = mutableListOf<Experience>()

    fun analyzer() {
        val newExp = mutableListOf<Experience>()
        Log.i("Response1 allExp", allExp.value.toString())
        allExp.value?.forEach { experience ->
            if (!processedExp.contains(experience)) {
                newExp.add(experience)
                processedExp.add(experience)
            }
        }

        GlobalScope.launch {
            newExp.forEach { experience1 ->
                launch {
                    callApi(experience1.situation) { result ->
                        when (result.toUpperCase()) {
                            "WORK" -> workList.add(experience1)
                            "MEANING" -> meaningList.add(experience1)
                            "COMMUNICATION" -> communicationList.add(experience1)
                            "DISCIPLINE" -> disciplineList.add(experience1)
                            "LEARNING" -> learningList.add(experience1)
                            "RELATIONSHIP" -> relationshipList.add(experience1)
                        }
                    }
                    delay(2000)
                }
            }
        }
        Log.i("Response5-1", workList.toString())
        Log.i("Response5-2", meaningList.toString())
        Log.i("Response5-3", communicationList.toString())
        Log.i("Response5-4", disciplineList.toString())
        Log.i("Response5-5", learningList.toString())
        Log.i("Response5-6", relationshipList.toString())
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

    fun callApi(question: String, callback: (String) -> Unit) {
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
                handleApiResponse(response, callback)
            } catch (e: SocketTimeoutException) {
                addResponse("Timeout: $e")
            }
        }
    }

    private suspend fun handleApiResponse(
        response: Response<CompletionResponse>,
        callback: (String) -> Unit
    ) {
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                response.body()?.let { completionResponse ->
                    val result = completionResponse.choices.firstOrNull()?.text
                    if (result != null) {
                        callback(result.trim())
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


    fun clearDB() {
        viewModelScope.launch {
            repository.clearExpInRoom()
        }
    }


    fun getCurrentTimestamp(): String {
        return SimpleDateFormat("hh mm a", Locale.getDefault()).format(Date())
    }


}