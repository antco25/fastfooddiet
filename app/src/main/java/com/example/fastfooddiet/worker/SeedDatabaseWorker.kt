package com.example.fastfooddiet.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.fastfooddiet.R
import com.example.fastfooddiet.data.AppDatabase
import com.example.fastfooddiet.data.Food
import com.example.fastfooddiet.data.Nutrition
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.coroutineScope
import java.lang.Exception


/**
 * Parse Food Data JSON text asset using GSON then upload into
 * Room Database using WorkManager and coroutines.
 * Also upload default Nutrition Data into database
 * Called on database creation via callback - see AppDatabase
 */

class SeedDatabaseWorker(context: Context, params : WorkerParameters)
    : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = coroutineScope {
        Log.d("Logger", "SeedDatabaseWorker called")

        seedFoodData(applicationContext)
        seedNutritionData(applicationContext)
        Result.success()
    }

    private suspend fun seedFoodData(applicationContext: Context) {
        //Parse JSON
        try {
            applicationContext.assets.open("mcd-pretty-test.json").use { inputStream ->
                JsonReader(inputStream.reader()).use { reader ->

                    val type = object : TypeToken<List<Food>>() {}.type
                    val foods: List<Food> = Gson().fromJson(reader, type)

                    AppDatabase.getDatabase(applicationContext).foodDao().insertFoods(foods)
                    foods.forEach { Log.d("Logger", it.name) }
                }
            }
        }
        catch (e : Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    private suspend fun seedNutritionData(applicationContext: Context) {
        val masterData = Nutrition(id = 0,
            calories = 2000,
            fat = 65,
            satfat = 20,
            transfat = 2,
            chol = 300,
            sodium = 2400,
            carbs = 300,
            sugar = 38,
            fiber = 25,
            protein = 64)

        val customData = Nutrition(id = 1,
            calories = 1000,
            fat = 65,
            satfat = 20,
            transfat = 2,
            chol = 300,
            sodium = 2400,
            carbs = 300,
            sugar = 38,
            fiber = 25,
            protein = 64)

        //Set master data as default in shared pref
        val sharedPref = applicationContext.getSharedPreferences(
            applicationContext.resources.getString(R.string.preference_file_key),
            Context.MODE_PRIVATE)

        with (sharedPref.edit()) {
            putInt(applicationContext.resources.getString(R.string.nutrition_key), 0)
            commit()
        }

        //Save to database
        AppDatabase.getDatabase(applicationContext).nutritionDao().insertNutrition(masterData)
        AppDatabase.getDatabase(applicationContext).nutritionDao().insertNutrition(customData)
    }
}

