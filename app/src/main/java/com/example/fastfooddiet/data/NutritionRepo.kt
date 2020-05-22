package com.example.fastfooddiet.data

import androidx.lifecycle.LiveData


class NutritionRepo(private val nutritionDao: NutritionDao) {

    fun getMasterNutrition() : LiveData<Nutrition> {
        return nutritionDao.getMasterNutrition()
    }

    fun getCustomNutrition() : LiveData<Nutrition> {
        return nutritionDao.getCustomNutrition()
    }

    suspend fun updateCustomNutrition(nutrition: Nutrition) {
        if (nutrition.id != 1)
            return

        nutritionDao.updateNutrition(nutrition)
    }

    suspend fun getMasterNutritionRaw() : Nutrition {
        return nutritionDao.getMasterNutritionRaw()
    }

}