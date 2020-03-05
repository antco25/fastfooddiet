package com.example.fastfooddiet.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.fastfooddiet.SearchListFragment.SearchType
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

    fun search(query : String?) {
        query?.let {
            if (!it.isBlank())
                searchQuery.value = "%$query%"
            else
                searchQuery.value = "%%"
        }
    }

    val foodResults : LiveData<List<Food>> = searchQuery.switchMap {
        foodRepo.searchFoods(it)
    }
    val stringResults : LiveData<List<String>> = searchQuery.switchMap {
        foodRepo.searchRestaurants(it)
    }
}