package com.example.fastfooddiet.data

import androidx.lifecycle.LiveData

class FoodRepo(private val foodDao: FoodDao) {

    val getFoods : LiveData<List<Food>> = foodDao.getFoods()

    fun searchFoods(query : String) : LiveData<List<Food>> {
        return foodDao.searchFoods(query)
    }



}