package com.example.fastfooddiet.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.fastfooddiet.data.AppDatabase
import com.example.fastfooddiet.data.FoodRepo

class AdvancedSearchViewModel (application: Application) : AndroidViewModel(application) {

    private val foodRepo : FoodRepo

    init {
        val foodDao = AppDatabase.getDatabase(application).foodDao()
        foodRepo = FoodRepo(foodDao)
    }

}