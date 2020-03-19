package com.example.fastfooddiet.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.fastfooddiet.data.AppDatabase
import com.example.fastfooddiet.data.Food
import com.example.fastfooddiet.data.FoodRepo
import com.example.fastfooddiet.data.SearchParams

class FoodListViewModel (application: Application) : AndroidViewModel(application) {

    private val foodRepo : FoodRepo

    init {
        val foodDao = AppDatabase.getDatabase(application).foodDao()
        foodRepo = FoodRepo(foodDao)
    }

    var showAllResultsDefault = false
    private val searchQuery = MutableLiveData<String>()

    fun search(query : String?) {
        query?.let {
            Log.d("Logger", "New Query: ${it}")
            if (!it.isBlank())
                searchQuery.value = "%$query%"
            else if (showAllResultsDefault)
                searchQuery.value = "%%"
            else
                searchQuery.value = ""
        }
    }

    fun getSearchQuery() : String {
        searchQuery.value?.let {string ->
            return string.substring(1, string.length-1)
        }
        return ""
    }

    fun getFoodResults(isFavorite : Boolean, searchParams: SearchParams?): LiveData<List<Food>> {
        searchParams?.let {
            return getFilteredFoodResults(it)
        }

        return when (isFavorite) {
            true -> searchQuery.switchMap { foodRepo.searchFavoriteFoods(it) }
            false -> searchQuery.switchMap { foodRepo.searchFoods(it) }
        }
    }

    private val _filteredSearchQuery = MutableLiveData<SupportSQLiteQuery>()
    var filteredSearchQuery = ""

    fun filteredSearch(query:String?, searchParams: SearchParams) {
        query?.let {
            if (!it.isBlank()) {
                val updatedSearchParams = searchParams.copy(query = it)
                _filteredSearchQuery.value = foodRepo.rawQueryBuilder(updatedSearchParams)
            } else
                _filteredSearchQuery.value = foodRepo.rawQueryBuilder(searchParams)

            filteredSearchQuery = it
        }
    }

    private fun getFilteredFoodResults(searchParams: SearchParams) : LiveData<List<Food>> {
        return _filteredSearchQuery.switchMap { foodRepo.filteredSearch(it) }
    }

    override fun onCleared() {
        Log.d("Logger", "FoodListViewModel cleared")
        super.onCleared()
    }
}