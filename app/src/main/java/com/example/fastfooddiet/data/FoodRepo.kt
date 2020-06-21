package com.example.fastfooddiet.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

class FoodRepo(private val foodDao: FoodDao) {

    fun getFood(id : Int) : LiveData<Food> {
        return foodDao.getFood(id)
    }

    suspend fun getFoodSizes(name : String, restaurant: String) : List<FoodSize> {
        return foodDao.getFoodSizes(name, restaurant)
    }

    fun browseFoods(query : String, restaurant: String, foodType: String) : LiveData<List<Food>> {
        return foodDao.browseFoods(query, restaurant, foodType)
    }

    fun searchFoodsOneSize(query : String) : LiveData<List<Food>> {
        return foodDao.searchFoodsOneSize(query)
    }

    fun searchFavoriteFoods(query : String) : LiveData<List<Food>> {
        return foodDao.searchFavoriteFoods(query)
    }

    fun getRestaurantCategory() : LiveData<List<Restaurant>> {
        return foodDao.getRestaurantCategory()
    }

    fun searchRestaurants(query : String) : LiveData<List<Restaurant>> {
        return foodDao.searchRestaurants(query)
    }

    fun searchFoodType(query: String) : LiveData<List<FoodType>> {
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
            it.map { restaurant ->
                string += "'${restaurant.replace("'","''")}',"
            }
            string = string.dropLast(1)
            string += ") "
        }

        //Set food type filter
        searchParams.foodType?.let {
            string += "AND foodType IN ("
            it.map { foodType ->
                string += "'${foodType.replace("'","''")}',"
            }
            string = string.dropLast(1)
            string += ") "
        }

        //Set favorite filter
        if (searchParams.favorites == 1) {
            string += "AND favorite = 1 "
        }

        //Set nutrition filters
        string += nutritionFilterText("calories", searchParams.caloriesMax, searchParams.caloriesMin)
        string += nutritionFilterText("protein", searchParams.proteinMax, searchParams.proteinMin)
        string += nutritionFilterText("fat", searchParams.fatMax, searchParams.fatMin)
        string += nutritionFilterText("satfat", searchParams.sfatMax, searchParams.sfatMin)
        string += nutritionFilterText("transfat", searchParams.tfatMax, searchParams.tfatMin)
        string += nutritionFilterText("chol", searchParams.cholMax, searchParams.cholMin)
        string += nutritionFilterText("sodium", searchParams.sodiumMax, searchParams.sodiumMin)
        string += nutritionFilterText("carbs", searchParams.carbMax, searchParams.carbMin)
        string += nutritionFilterText("sugar", searchParams.sugarMax, searchParams.sugarMin)
        string += nutritionFilterText("fiber", searchParams.fiberMax, searchParams.fiberMin)

        string += "ORDER BY name ASC"

        Log.d("Logger", string)

        return SimpleSQLiteQuery(string)
    }

    private fun nutritionFilterText(name : String, max : Float, min : Float) : String {

        var string = ""

        if (max > 0 && min > 0)
            string += "AND $name BETWEEN $min and $max "
        else if (min > 0)
            string += "AND $name >= $min "
        else if (max > 0)
            string += "AND $name BETWEEN 0 and $max "

        return string
    }

    private fun nutritionFilterText(name : String, max : Int, min : Int) : String {
        return nutritionFilterText(name, max.toFloat(), min.toFloat())
    }
}