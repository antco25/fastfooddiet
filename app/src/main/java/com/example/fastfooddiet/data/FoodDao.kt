package com.example.fastfooddiet.data

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface FoodDao {

    //****FOOD RELATED ******

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoods(foods : List<Food>)

    @Query("SELECT * from food_table WHERE foodId = :id")
    fun getFood(id : Int) : LiveData<Food>

    @Query("SELECT foodId, size from food_table WHERE name = :name AND restaurant = :restaurant ORDER BY foodId ASC")
    suspend fun getFoodSizes(name : String, restaurant: String) : List<FoodSize>

    @Query("SELECT * from food_table WHERE name LIKE :query AND restaurant = :restaurant AND foodType = :foodType AND sizeMode != 2 ORDER BY name ASC")
    fun browseFoods(query : String, restaurant: String, foodType: String) : LiveData<List<Food>>

    @Query("SELECT * from food_table WHERE name LIKE :query AND sizeMode != 2 ORDER BY name ASC")
    fun searchFoodsOneSize(query : String) : LiveData<List<Food>>

    @Query("SELECT * from food_table WHERE name LIKE :query AND favorite = 1 ORDER BY name ASC")
    fun searchFavoriteFoods(query : String) : LiveData<List<Food>>

    @RawQuery(observedEntities = [Food::class])
    fun searchFilteredFoods(dbQuery : SupportSQLiteQuery) : LiveData<List<Food>>

    @Query("UPDATE food_table SET favorite = :isFavorite WHERE foodId = :id")
    suspend fun setFavorite(id : Int, isFavorite : Boolean)

    //****CATEGORY RELATED******

    @Query("""SELECT DISTINCT restaurant from food_table 
        WHERE restaurant LIKE :query ORDER BY restaurant ASC""")
    fun searchRestaurants(query : String) : LiveData<List<Restaurant>>

    @Query("""SELECT DISTINCT foodType from food_table 
        WHERE restaurant LIKE :query ORDER BY foodType ASC""")
    fun searchFoodType(query : String) : LiveData<List<FoodType>>

    @Query("SELECT DISTINCT restaurant from food_table ORDER BY restaurant ASC")
    fun getAllRestaurants() : LiveData<Array<String>>

    @Query("SELECT DISTINCT restaurant from food_table ORDER BY restaurant ASC")
    fun getRestaurantCategory() : LiveData<List<Restaurant>>

    @Query("SELECT DISTINCT foodType from food_table ORDER BY foodType ASC")
    fun getAllFoodTypes() : LiveData<Array<String>>
}

//@Query("SELECT * from food_table WHERE name LIKE :query AND calories BETWEEN 8 and 8 AND ")