package com.example.fastfooddiet.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.fastfooddiet.data.AppDatabase
import com.example.fastfooddiet.data.Category
import com.example.fastfooddiet.data.FoodRepo

class CategoryViewModel (application: Application) : AndroidViewModel(application) {

    private val foodRepo : FoodRepo

    init {
        AppDatabase.getDatabase(application).apply {
            foodRepo = FoodRepo(this.foodDao())
        }
    }

    var header = MutableLiveData<String>("Select a Restaurant")

    /*
     * FETCHING CATEGORY DATA FROM DATABASE TODO: DELETE?
     */

    /*
    private data class CategoryQuery (
        var type : CategoryType,
        var query : String
    )

    private var _dbQuery = CategoryQuery(CategoryType.RESTAURANT, "%%")
    private val dbQuery = MutableLiveData<CategoryQuery>(_dbQuery)

    val categories : LiveData<List<Category>> = dbQuery.switchMap { categoryQuery ->
        when (categoryQuery.type) {
            CategoryType.RESTAURANT -> foodRepo.searchRestaurants(categoryQuery.query)
            CategoryType.FOOD_TYPE -> foodRepo.searchFoodType(selectedRestaurant!!)
        } as LiveData<List<Category>>
    }

    fun setCategoryQuery(rawQuery : String) {
        _dbQuery.query = if (rawQuery.isBlank()) "%%" else "%$rawQuery%"
        dbQuery.value = _dbQuery
    }

    fun setCategory(type : CategoryType) {

        if (type == CategoryType.RESTAURANT)
            header.value = "Select a Restaurant"

        _dbQuery.type = type
        dbQuery.value = _dbQuery
    }

    fun getCategory() : CategoryType {
        return _dbQuery.type
    }





    /*
    * ON ITEM CATEGORY CLICK
    */

    var selectedRestaurant : String? = null
        private set

    fun setRestaurant(restaurant: String) {
        selectedRestaurant = restaurant
        header.value = "$restaurant - Select a Food Category"
        _dbQuery.type = CategoryType.FOOD_TYPE
        dbQuery.value = _dbQuery
    }
    */


    /*
    * HOME & FOOD TYPE FRAGMENT
    */

    val restaurants : LiveData<List<Category>> = Transformations
        .map(foodRepo.getRestaurantCategory()) { list -> list as List<Category> }

    val setRestaurant = MutableLiveData<String>()
    val foodTypes : LiveData<List<Category>> =  setRestaurant.switchMap { restaurant ->
        Transformations.map(foodRepo.searchFoodType(restaurant)) { list ->
            list as List<Category>
        }
    }


}