package com.example.fastfooddiet.data

import android.util.Log
import androidx.lifecycle.LiveData

class FoodRepo(private val foodDao: FoodDao) {

    fun getFood(id : Int) : LiveData<Food> {
        return foodDao.getFood(id)
    }

    fun searchFoods(query : String) : LiveData<List<Food>> {
        return foodDao.searchFoods(query)
    }

    fun searchRestaurants(query : String) : LiveData<List<String>> {
        return foodDao.searchRestaurants(query)
    }
}