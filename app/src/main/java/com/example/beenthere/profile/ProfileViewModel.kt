package com.example.beenthere.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.beenthere.data.User
import com.example.beenthere.data.source.BeenThereRepository
import com.example.beenthere.model.openai.Message
import com.example.beenthere.utils.UserManager

class ProfileViewModel(private val repository: BeenThereRepository) : ViewModel() {


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

//    fun getUserName(name: String) {
//        _name.value = name
//    }
//
//    fun getUserAvatar(avatar: String) {
//        _avatar.value = avatar
//    }

    // Store the user ID when the user logs in






}