package com.example.fastfooddiet.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.fastfooddiet.data.AppDatabase
import com.example.fastfooddiet.data.FoodRepo
import com.example.fastfooddiet.view.CategoryListFragment.Category

class CategoryListViewModel (application: Application) : AndroidViewModel(application) {

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

    fun getCategoryResults(category : Category): LiveData<List<String>> {
        return when (category) {
            Category.RESTAURANT -> searchQuery.switchMap { foodRepo.searchRestaurants(it) }
            Category.FOOD_TYPE -> searchQuery.switchMap { foodRepo.searchFoodType(it) }
        }
    }

}