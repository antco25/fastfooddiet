package com.example.fastfooddiet.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.fastfooddiet.data.MealData
import kotlinx.android.synthetic.main.fragment_favorite.view.*

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

    //**** SHARED TO: SETTINGS NUMBER DIALOG ****
    val numberPair = MutableLiveData<Pair<String, Int>>()

    //**** SHARED TO: TEXT INPUT DIALOG ****
    var textChanged = false
    val textInput = MutableLiveData<String>()

    //**** SHARED TO: MEAL DIALOG ****
    var isAddMeal = false
    var mealDataChanged = false
    val mealData = MutableLiveData<MealData>()

}