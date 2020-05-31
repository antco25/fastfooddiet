package com.example.fastfooddiet.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.fastfooddiet.worker.SeedDatabaseWorker

@Database(entities = [Food::class, MealData::class, MealFoodCrossRef::class, Nutrition::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun foodDao() : FoodDao
    abstract fun nutritionDao() : NutritionDao
    abstract fun mealDao() : MealDao

    companion object {
        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getDatabase(context : Context) : AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        //TODO: Database is lazy loaded so if user uses custom search, it won't load restaurants and food types?
        //Create database and prepopulate it via callback
        //See Google "Sunflower" app for reference
        private fun buildDatabase(context: Context) : AppDatabase {
            return Room.databaseBuilder(context.applicationContext,
                AppDatabase::class.java, "app_database")
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
                        WorkManager.getInstance(context).enqueue(request)
                    }
                })
                .build()
        }
    }
}