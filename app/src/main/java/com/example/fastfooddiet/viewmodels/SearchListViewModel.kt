package com.example.fastfooddiet.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.fastfooddiet.data.AppDatabase
import com.example.fastfooddiet.data.Food
import com.example.fastfooddiet.data.FoodRepo

class SearchListViewModel (application: Application) : AndroidViewModel(application) {

    private val foodRepo : FoodRepo

    init {
        val foodDao = AppDatabase.getDatabase(application).foodDao()
        foodRepo = FoodRepo(foodDao)
    }

    private val searchQuery = MutableLiveData<String>()
    val searchResults : LiveData<List<Food>> = searchQuery.switchMap {
        foodRepo.searchFoods(it)
    }

    fun search(query : String?) {
        query?.let {
            if (query.length == 0)
                //Display nothing when query is empty
                searchQuery.value = ""
            else
                searchQuery.value = "%$query%"
        }

    }
}