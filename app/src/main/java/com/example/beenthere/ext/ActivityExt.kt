package com.example.beenthere.ext

import android.app.Activity
import android.view.Gravity
import android.widget.Toast

import com.example.beenthere.BeenThereApplication
import com.example.beenthere.factory.ViewModelFactory

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Extension functions for Activity.
 */
fun Activity.getVmFactory(): ViewModelFactory {
    val repository = (applicationContext as BeenThereApplication).beenThereRepository
    return ViewModelFactory(repository)
}

fun Activity?.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).apply {
        setGravity(Gravity.CENTER, 0, 0)
        show()
    }
}
