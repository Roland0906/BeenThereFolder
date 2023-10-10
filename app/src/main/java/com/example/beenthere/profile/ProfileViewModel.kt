package com.example.beenthere.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.beenthere.data.Experience
import com.example.beenthere.data.LikedExp
import com.example.beenthere.data.LiveTalkEvent
import com.example.beenthere.data.User
import com.example.beenthere.data.source.BeenThereRepository
import com.example.beenthere.utils.UserManager
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel(private val repository: BeenThereRepository) : ViewModel() {

    private val db = Firebase.firestore

    private val _name = MutableLiveData<String>()
    val name: LiveData<String>
        get() = _name

    private val _avatar = MutableLiveData<String>()
    val avatar: LiveData<String>
        get() = _avatar



    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean>
        get() = _isLoggedIn


    init {
        _name.value =
            if (UserManager.userName == "null" || UserManager.userName == "") {
            ""
        } else {
            UserManager.userName
            }
        _avatar.value = UserManager.userAvatar



    }

    fun getUser(user: User) {
        _name.value = if (UserManager.userName == "null" || UserManager.userName == "") {
            ""
        } else {
            UserManager.userName
        }
        _avatar.value = user.userAvatar.toString()

    }

    fun getUserName(name: String) {
        _name.value = name
    }

    fun getUserAvatar(avatar: String) {
        _avatar.value = avatar
    }

    private val _navigateToDetail = MutableLiveData<Experience>()

    val navigateToDetail: LiveData<Experience>
        get() = _navigateToDetail

    fun navigateToDetail(experience: Experience) {
        _navigateToDetail.value = experience
    }

    fun onDetailNavigated() {
        _navigateToDetail.value = null
    }

    // Store the user ID when the user logs in



    private var expListener: ListenerRegistration? = null

    private val _likedExp = MutableStateFlow<List<LikedExp>>(emptyList())

    // Expose the Flow as a StateFlow for read-only access
    val likedExp: StateFlow<List<LikedExp>> = _likedExp.asStateFlow()

    fun setExpListener(userId: String) {

        Log.i("Profile VM user id", userId)

        try {


            val favoriteDoc =
                db.collection("users").document(userId).collection("favorite")

            var expList: MutableList<LikedExp> = mutableListOf()

            expListener = favoriteDoc.addSnapshotListener { snapShot, e ->
                if (e != null) {

                    return@addSnapshotListener

                } else {
                    if (snapShot != null && snapShot.size() != 0) {
                        if (snapShot.first().get("experience") != null && snapShot.first()
                                .get("experience") != ""
                        ) {
                            for (document in snapShot) {
                                val exp = document.toObject<LikedExp>()

                                Log.i("Profile VM liked exp", exp.toString())

                                expList.add(exp)
                            }
                            _likedExp.value = expList
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.i("Profile VM setListener", e.message.toString())
        }
    }

    override fun onCleared() {
        super.onCleared()
        expListener?.remove()
    }

}