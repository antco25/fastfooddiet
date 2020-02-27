package com.example.fastfooddiet.data

import android.util.Log
import androidx.lifecycle.LiveData

class FoodRepo(private val foodDao: FoodDao) {

    val getFoods : LiveData<List<Food>> = foodDao.getFoods()

    fun getFood(id : Int) : LiveData<Food> {
        return foodDao.getFood(id)
    }

    fun searchFoods(query : String) : LiveData<List<Food>> {
        return foodDao.searchFoods(query)
    }



}