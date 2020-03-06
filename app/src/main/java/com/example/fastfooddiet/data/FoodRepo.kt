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

    fun searchFavoriteFoods(query : String) : LiveData<List<Food>> {
        return foodDao.searchFavoriteFoods(query)
    }

    fun searchRestaurants(query : String) : LiveData<List<String>> {
        return foodDao.searchRestaurants(query)
    }

    fun searchFoodType(query: String) : LiveData<List<String>> {
        return foodDao.searchFoodType(query)
    }

    suspend fun setFavorite(id : Int, isFavorite : Boolean) {
        foodDao.setFavorite(id, isFavorite)
    }

    //TODO: custom query
    fun advancedSearch(query : String, searchParams: SearchParams) {
        var queryString = "SELECT * from food_table"
    }


    //"SELECT * from food_table WHERE name LIKE :query AND favorite = 1 ORDER BY name ASC
}