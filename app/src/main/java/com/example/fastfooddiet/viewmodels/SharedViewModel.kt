package com.example.fastfooddiet.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel

/*
 * ViewModel to share data between fragments
 * Fragments should be in a nested graph and viewmodel scoped to nested graph
 */
class SharedViewModel (application: Application) : AndroidViewModel(application) {
    init {
        Log.d("Logger", "Shared View Model created")
    }

    override fun onCleared() {
        Log.d("Logger", "Shared View Model cleared")
        super.onCleared()
    }

    var selectedRestaurant : List<String>? = null
    var selectedFoodTypes : List<String>? = null

    var randomText : String? = null

}