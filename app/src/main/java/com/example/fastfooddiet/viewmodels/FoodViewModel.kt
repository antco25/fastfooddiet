package com.example.fastfooddiet.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.fastfooddiet.data.AppDatabase
import com.example.fastfooddiet.data.Food
import com.example.fastfooddiet.data.FoodRepo
import kotlinx.coroutines.launch

//TODO: For detail view
class FoodViewModel(application: Application) : AndroidViewModel(application) {

    private val foodRepo : FoodRepo

    init {
        val foodDao = AppDatabase.getDatabase(application).foodDao()
        foodRepo = FoodRepo(foodDao)
    }

}