package com.example.beenthere.util

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.example.beenthere.data.source.BeenThereRemoteDataSource

import com.example.beenthere.data.source.BeenThereRepository


/**
 * A Service Locator for the [StylishRepository].
 */
object ServiceLocator {

    @Volatile
    var beenThereRepository: BeenThereRepository? = null
        @VisibleForTesting set

    fun provideTasksRepository(context: Context): BeenThereRepository {
        synchronized(this) {
            return beenThereRepository
                ?: createBeenThereRepository(context)
        }
    }

    private fun createBeenThereRepository(context: Context): BeenThereRepository {
        return BeenThereRemoteDataSource
    }

}
