package com.example.beenthere.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beenthere.data.Experience
import com.example.beenthere.data.LiveTalkEvent
import com.example.beenthere.data.openai.ApiClient
import com.example.beenthere.data.source.BeenThereRepository
import com.example.beenthere.model.openai.CompletionRequest
import com.example.beenthere.model.openai.CompletionResponse
import com.example.beenthere.model.openai.Message
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class CATEGORY {
    LIFE_MEANING,
    COMMUNICATION,
    DISCIPLINE,
    LEARNING,
    WORK,
    RELATIONSHIP
}

class HomeViewModel(private val repository: BeenThereRepository) : ViewModel() {


    private val db = Firebase.firestore
    private val collectionExp = db.collection("experiences")

    private val _messageList = MutableLiveData<MutableList<Message>>()
    val messageList: LiveData<MutableList<Message>>
        get() = _messageList



    init {
        _messageList.value = mutableListOf()
        setFirebaseListener()
//        liveTalkListener = liveTalkDoc.addSnapshotListener { snapShot, e ->
//            val events = snapShot?.documents?.map { it.toObject<LiveTalkEvent>() }
//            _liveTalkEvents.value = (events ?: emptyList<LiveTalkEvent>()) as List<LiveTalkEvent>
//        }
        eventListener()
    }

    fun allExp(): Flow<List<Experience>> = repository.getExp()

    suspend fun analyzer(expList: List<Experience>) {

        Log.i("Response1 allExp", expList.toString())

        expList.forEach { it ->
            if (!it.isProcessed) {
                callApi(it.situation) { result ->
                    viewModelScope.launch {
                        when (result.toUpperCase()) {

                            CATEGORY.WORK.toString() -> repository.updateExp(it.copy(category = CATEGORY.WORK.toString()))
                            CATEGORY.LIFE_MEANING.toString() -> repository.updateExp(
                                it.copy(
                                    category = CATEGORY.LIFE_MEANING.toString()
                                )
                            )

                            CATEGORY.COMMUNICATION.toString() -> repository.updateExp(
                                it.copy(
                                    category = CATEGORY.COMMUNICATION.toString()
                                )
                            )

                            CATEGORY.DISCIPLINE.toString() -> repository.updateExp(it.copy(category = CATEGORY.DISCIPLINE.toString()))
                            CATEGORY.LEARNING.toString() -> repository.updateExp(it.copy(category = CATEGORY.LEARNING.toString()))
                            CATEGORY.RELATIONSHIP.toString() -> repository.updateExp(
                                it.copy(
                                    category = CATEGORY.RELATIONSHIP.toString()
                                )
                            )
                        }
                    }
                }
                val newExp = it.copy(isProcessed = true)
                repository.updateExp(newExp)
                delay(3000)
            }
        }

    }

    private var snapshotListener: ListenerRegistration? = null
    private fun setFirebaseListener() {

//        collection.addSnapshotListener { snapShot, e ->
        snapshotListener = collectionExp.addSnapshotListener { snapShot, e ->
            if (e != null) {

                return@addSnapshotListener

            } else {
                if (snapShot != null && snapShot.size() != 0) {
                    if (snapShot.first().get("situation") != null && snapShot.first()
                            .get("situation") != ""
                    ) {
                        for (document in snapShot) {
                            val experience = document.toObject<Experience>()
                            Log.i("Experience", experience.toString())
                            viewModelScope.launch {
                                // change to groovy to use python
                                // use open ai to convert python response to kotlin class
                                // check if already in Room before insert?

                                try {
                                    allExp().collect {
                                        if (!it.contains(experience)) {
                                            repository.insertExp(experience)
                                        }
                                    }

                                } catch (e: Exception) {
                                    Log.i("Insert FB data", e.message.toString())
                                }

                            }
                        }
                    }
                }
            }
        }
    }

    fun removeFirebaseListeners() {
        snapshotListener?.remove()
        liveTalkListener?.remove()
    }


    private fun addToChat(message: String, sentBy: String, timestamp: String) {
        val currentList = _messageList.value ?: mutableListOf()
        currentList.add(Message(message, sentBy, timestamp))
        _messageList.postValue(currentList)
    }

    private fun addResponse(response: String) {
        _messageList.value?.removeAt(
            _messageList.value?.size?.minus(1) ?: 0
        )
        addToChat(response, Message.SENT_BY_BOT, getCurrentTimestamp())
    }


    val categories =
        CATEGORY.entries // Log(toString()) : [MEANING, COMMUNICATION, DISCIPLINE, LEARNING, WORK, RELATIONSHIP]


    private val checkCategoryString =
        "Which one of the 6 types $categories does the following description belong to:"

    private fun callApi(question: String, callback: (String) -> Unit) {
        addToChat("Typing...", Message.SENT_BY_BOT, getCurrentTimestamp())

        Log.i("Response2", "callApi")
        val completionRequest = CompletionRequest(
            model = "text-davinci-003",
            prompt = checkCategoryString + question,
            max_tokens = 1000
        )

        viewModelScope.launch {
            try {
                val response =
                    ApiClient.apiService.getCompletions(completionRequest)
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
                    val result =
                        completionResponse.choices.firstOrNull()?.text
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


    private fun getCurrentTimestamp(): String {
        return SimpleDateFormat(
            "hh mm a",
            Locale.getDefault()
        ).format(Date())
    }





    val toastMessageLiveData = MutableLiveData<String?>()

    private fun showMessage(message: String?) {
        toastMessageLiveData.value = message
    }

    private val _liveTalkEvents = MutableStateFlow<List<LiveTalkEvent>>(emptyList())

    // Expose the Flow as a StateFlow for read-only access
    val liveTalkEvents: StateFlow<List<LiveTalkEvent>> = _liveTalkEvents.asStateFlow()

    private var liveTalkListener: ListenerRegistration? = null
    private fun eventListener() {

        var tempList: MutableList<LiveTalkEvent> = mutableListOf()

        val liveTalkDoc = db.collection("live_talks")

        liveTalkListener = liveTalkDoc.addSnapshotListener { snapShot, e ->
            if (e != null) {

                return@addSnapshotListener

            } else {
                if (snapShot != null && snapShot.size() != 0) {
                    if (snapShot.first().get("topic") != null && snapShot.first()
                            .get("topic") != ""
                    ) {
                        for (document in snapShot) {
                            val event = document.toObject<LiveTalkEvent>()
                            Log.i("HomeVM live talk event", event.toString())

                            tempList.add(event)
                        }
                        _liveTalkEvents.value = tempList
                    }
                }
            }
        }
    }


}






