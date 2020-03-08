package com.example.fastfooddiet.viewmodels

import android.app.Application
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
            if (!it.isBlank())
                searchQuery.value = "%$query%"
            else if (showAllResultsDefault)
                searchQuery.value = "%%"
            else
                searchQuery.value = ""
        }
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

    private val filteredSearchQuery = MutableLiveData<SupportSQLiteQuery>()

    fun filteredSearch(query:String?, searchParams: SearchParams) {
        query?.let {
            if (!it.isBlank()) {
                val updatedSearchParams = searchParams.copy(query = it)
                filteredSearchQuery.value = foodRepo.rawQueryBuilder(updatedSearchParams)
            } else
                filteredSearchQuery.value = foodRepo.rawQueryBuilder(searchParams)
        }
    }

    private fun getFilteredFoodResults(searchParams: SearchParams) : LiveData<List<Food>> {
        return filteredSearchQuery.switchMap { foodRepo.filteredSearch(it) }
    }
}