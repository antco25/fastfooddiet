package com.example.fastfooddiet.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoods(foods : List<Food>)

    @Query("SELECT * from food_table WHERE id = :id")
    fun getFood(id : Int) : LiveData<Food>

    @Query("SELECT * from food_table WHERE name LIKE :query ORDER BY name ASC")
    fun searchFoods(query : String) : LiveData<List<Food>>

    @Query("SELECT * from food_table WHERE name LIKE :query AND favorite = 1 ORDER BY name ASC")
    fun searchFavoriteFoods(query : String) : LiveData<List<Food>>

    @Query("""SELECT DISTINCT restaurant from food_table 
        WHERE restaurant LIKE :query ORDER BY restaurant ASC""")
    fun searchRestaurants(query : String) : LiveData<List<String>>

    @Query("""SELECT DISTINCT foodType from food_table 
        WHERE foodType LIKE :query ORDER BY foodType ASC""")
    fun searchFoodType(query : String) : LiveData<List<String>>

    @Query("UPDATE food_table SET favorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id : Int, isFavorite : Boolean)
}