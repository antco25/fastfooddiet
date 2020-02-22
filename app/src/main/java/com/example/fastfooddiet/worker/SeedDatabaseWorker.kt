package com.example.fastfooddiet.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.fastfooddiet.data.AppDatabase
import com.example.fastfooddiet.data.Food
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.coroutineScope
import java.lang.Exception


/**
 * Parse JSON text asset using GSON then upload into
 * Room Database using WorkManager and coroutines
 * Called on database creation via callback - see AppDatabase
 */

class SeedDatabaseWorker(context: Context, params : WorkerParameters)
    : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = coroutineScope {
        Log.d("Logger", "SeedDatabaseWorker called")

        //Parse JSON
        try {
            applicationContext.assets.open("mcd-pretty-test.json").use { inputStream ->
                JsonReader(inputStream.reader()).use { reader ->
                    val type = object : TypeToken<List<Food>>() {}.type
                    val foods: List<Food> = Gson().fromJson(reader, type)

                    //Insert into database
                    AppDatabase.getDatabase(applicationContext).foodDao().insertFoods(foods)

                    foods.forEach { Log.d("Logger", it.name + " version 2") }
                }
            }
            Result.success()
        }
        catch (e : Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}
