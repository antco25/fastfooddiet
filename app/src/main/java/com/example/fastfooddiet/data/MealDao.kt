package com.example.fastfooddiet.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MealDao {

    //*** MEAL ****
    @Transaction
    @Query("SELECT * from meal_table")
    fun getMeals() : LiveData<List<Meal>>

    @Transaction
    @Query("SELECT * from meal_table WHERE mealId = :id")
    fun getMeal(id: Int) : LiveData<Meal>

    //*** MEALDATA ****
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(mealData: MealData) : Long

    @Query("SELECT * from meal_table")
    fun getMealDatas() : LiveData<List<MealData>>

    @Query("DELETE from meal_table WHERE mealId = :mealId")
    suspend fun deleteMealData(mealId: Int)

    @Update
    suspend fun updateMealData(mealData: MealData)

    //*** MEAL FOOD ****
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMealFood(mealFood : MealFoodCrossRef)

    @Query("DELETE from meal_food_table WHERE mealId = :mealId")
    suspend fun deleteMealFoods(mealId: Int)

    @Query("DELETE from meal_food_table WHERE mealFoodId = :mealFoodId")
    suspend fun deleteMealFood(mealFoodId: Int)


}