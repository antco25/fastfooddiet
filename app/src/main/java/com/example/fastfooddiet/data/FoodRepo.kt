package com.example.fastfooddiet.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

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

    fun filteredSearch(dbQuery : SupportSQLiteQuery) : LiveData<List<Food>> {
        return foodDao.searchFilteredFoods(dbQuery)
    }

    fun rawQueryBuilder(searchParams: SearchParams) : SimpleSQLiteQuery {
        var string = "SELECT * from food_table WHERE name LIKE '%${searchParams.query}%' "

        //Set restaurant filter
        searchParams.restaurants?.let {
            string += "AND restaurant IN ("
            it.mapIndexed { index, restaurant ->
                string += "'${restaurant.replace("'","''")}'"
                string += if (index != it.size - 1) "," else ""
            }
            string += ") "
        }

        //Set food type filter
        searchParams.foodType?.let {
            string += "AND foodType IN ("
            it.mapIndexed { index, foodType ->
                string += "'${foodType.replace("'","''")}'"
                string += if (index != it.size - 1) "," else ""
            }
            string += ") "
        }

        //Set calorie filter
        if (searchParams.caloriesMax > 0 && searchParams.caloriesMin > 0)
            string += "AND calories BETWEEN ${searchParams.caloriesMin} and ${searchParams.caloriesMax} "
        else if (searchParams.caloriesMin > 0)
            string += "AND calories >= ${searchParams.caloriesMin} "
        else if (searchParams.caloriesMax > 0)
            string += "AND calories <= ${searchParams.caloriesMax} "

        string += "ORDER BY name ASC"

        Log.d("Logger", string)

        return SimpleSQLiteQuery(string)
    }
}