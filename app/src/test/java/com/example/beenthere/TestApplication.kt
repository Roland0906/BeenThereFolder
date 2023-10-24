package com.example.beenthere

import android.app.Application
import com.google.firebase.FirebaseApp

class TestApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}