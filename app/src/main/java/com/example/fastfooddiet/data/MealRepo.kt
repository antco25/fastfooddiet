package com.example.fastfooddiet.data

import androidx.lifecycle.LiveData


class MealRepo(private val mealDao: MealDao) {

    suspend fun insertMeal(mealData: MealData) : Long {
        return mealDao.insertMeal(mealData)
    }

    suspend fun insertMealFood(mealFood : MealFoodCrossRef) {
        mealDao.insertMealFood(mealFood)
    }

    fun getMeals() : LiveData<List<Meal>> {
        return mealDao.getMeals()
    }

    fun getMeal(id: Int) : LiveData<Meal> {
        return mealDao.getMeal(id)
    }

    fun getMealDatas() : LiveData<List<MealData>> {
        return mealDao.getMealDatas()
    }

    suspend fun updateMealData(mealData: MealData) {
        mealDao.updateMealData(mealData)
    }

    suspend fun deleteMealFoods(mealId: Int) {
        mealDao.deleteMealFoods(mealId)
    }

    suspend fun deleteMealFood(mealFoodId: Int) {
        mealDao.deleteMealFood(mealFoodId)
    }

    suspend fun deleteMealData(mealId: Int) {
        mealDao.deleteMealData(mealId)
    }

}