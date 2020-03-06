package com.example.fastfooddiet.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.fastfooddiet.data.AppDatabase
import com.example.fastfooddiet.data.Food
import com.example.fastfooddiet.data.FoodRepo
import com.example.fastfooddiet.data.SearchParams

class FoodListViewModel (application: Application) : AndroidViewModel(application) {

    private val foodRepo : FoodRepo

    init {
        val foodDao = AppDatabase.getDatabase(application).foodDao()
        foodRepo = FoodRepo(foodDao)
    }

    var showAllResultsDefault = false
    private val searchQuery = MutableLiveData<String>()

    fun search(query : String?) {
        query?.let {
            if (!it.isBlank())
                searchQuery.value = "%$query%"
            else if (showAllResultsDefault)
                searchQuery.value = "%%"
            else
                searchQuery.value = ""
        }
    }

    fun getFoodResults(isFavorite : Boolean): LiveData<List<Food>> {
        return when (isFavorite) {
            true -> searchQuery.switchMap { foodRepo.searchFavoriteFoods(it) }
            false -> searchQuery.switchMap { foodRepo.searchFoods(it) }
        }
    }







    /*
    fun getStringResults(searchType : SearchType) : LiveData<List<String>> {
        return searchQuery.switchMap {
            if (searchType == SearchType.FOOD_TYPE)
                foodRepo.searchFoodType(it)
            else
                foodRepo.searchRestaurants(it)
        }
    }

     */

    /*
    val stringResults : LiveData<List<String>> = searchQuery.switchMap {
        //foodRepo.searchRestaurants(it)
        foodRepo.searchFoodType(it)
    }

        val foodResults : LiveData<List<Food>> = searchQuery.switchMap {
        foodRepo.searchFoods(it, false)
    }

     */
}