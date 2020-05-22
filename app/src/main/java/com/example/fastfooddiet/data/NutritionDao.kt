package com.example.fastfooddiet.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NutritionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNutrition(nutrition: Nutrition)

    @Query("SELECT * from nutrition_table WHERE id = 0")
    fun getMasterNutrition() : LiveData<Nutrition>

    @Query("SELECT * from nutrition_table WHERE id = 0")
    suspend fun getMasterNutritionRaw() : Nutrition

    @Query("SELECT * from nutrition_table WHERE id = 1")
    fun getCustomNutrition() : LiveData<Nutrition>

    @Update
    suspend fun updateNutrition(nutrition: Nutrition)

}