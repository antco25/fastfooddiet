package com.example.fastfooddiet.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.fastfooddiet.data.MealData

/*
 * ViewModel to share data between fragments
 * Fragments should be in a nested graph and viewmodel scoped to nested graph
 */
class SharedViewModel (application: Application) : AndroidViewModel(application) {

    //**** SHARED TO: SETTINGS NUMBER DIALOG ****
    var settingsHandled = true
    //key (String), value (Float), isFloat (Boolean)
    val numberSetting = MutableLiveData<Triple<String, Float, Boolean>>()

    //**** SHARED TO: TEXT INPUT DIALOG ****
    var textChanged = false
    val textInput = MutableLiveData<String>()

    //**** SHARED TO: MEAL DIALOG ****
    var isAddMeal = false
    var mealDataChanged = false
    val mealData = MutableLiveData<MealData>()

    //**** SHARED TO: LIST DIALOG ****
    var listSelectionHandled = true
    val listSelection = MutableLiveData<Pair<String, Int?>>()

    //**** SHARED TO: FAVORITE, FAVORITE CHILD, MEAL & MEAL FOOD FRAGMENTS ****
    val isDeleteMode = MutableLiveData<Boolean>(false)
    private var _isDeleteMode = false

    fun isDeleteMode() : Boolean {
        return _isDeleteMode
    }

    fun setDeleteMode(isDeleteMode : Boolean) {
        _isDeleteMode = isDeleteMode
        this.isDeleteMode.value = isDeleteMode
    }

}