package com.example.beenthere

import android.app.Application
import com.example.beenthere.data.source.BeenThereRepository
import com.example.beenthere.utils.ServiceLocator

import kotlin.properties.Delegates

class BeenThereApplication : Application() {

    // Depends on the flavor,
    val beenThereRepository: BeenThereRepository
        get() = ServiceLocator.provideTasksRepository(this)

    companion object {
        var instance: BeenThereApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
