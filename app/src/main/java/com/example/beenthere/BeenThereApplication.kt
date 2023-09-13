package com.example.beenthere

import android.app.Application
import com.example.beenthere.data.source.BeenThereRepository
import com.example.beenthere.util.ServiceLocator

import kotlin.properties.Delegates

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * An application that lazily provides a repository. Note that this Service Locator pattern is
 * used to simplify the sample. Consider a Dependency Injection framework.
 */
class BeenThereApplication : Application() {

    // Depends on the flavor,
    val stylishRepository: BeenThereRepository
        get() = ServiceLocator.provideTasksRepository(this)

    companion object {
        var instance: BeenThereApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
