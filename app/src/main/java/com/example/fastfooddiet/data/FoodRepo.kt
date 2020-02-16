package com.example.fastfooddiet.data

import androidx.lifecycle.LiveData

class FoodRepo(private val foodDao: FoodDao) {

    val getFoods : LiveData<List<Food>> = foodDao.getFoods()

    suspend fun insertFoods(foods : List<Food>) {
        foodDao.insertFoods(foods)
    }

    suspend fun insertFood(food: Food) {
        foodDao.insertFood(food)
    }

}