package com.example.fastfooddiet.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fastfooddiet.data.AppDatabase
import com.example.fastfooddiet.data.Food
import com.example.fastfooddiet.data.FoodRepo

class SearchListViewModel (application: Application) : AndroidViewModel(application) {

    private val foodRepo : FoodRepo
    private val _searchQuery = MutableLiveData<String>()
    val searchQuery : LiveData<String> = _searchQuery

    private val _searchResults = MutableLiveData<List<String>>()
    val searchResults : LiveData<List<String>> = _searchResults
    val myFoods = mutableListOf<String>()

    init {
        val foodDao = AppDatabase.getDatabase(application).foodDao()
        foodRepo = FoodRepo(foodDao)
    }

    fun setSearchQuery( query : String) {
        _searchQuery.value = query
        Log.d("Logger","Query set")
    }

    fun search() {
        myFoods.add("Food " + myFoods.size)
        _searchResults.value = myFoods
    }
}