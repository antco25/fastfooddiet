package com.example.fastfooddiet.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "food_table")
data class Food (
    @PrimaryKey (autoGenerate = true)
    val id : Int,
    val name : String,
    val restaurant : String,
    val restaurantIcon : String,
    val foodType : String,
    val foodTypeIcon: String,
    val favorite : Boolean = false,
    val calories : Int,
    val servingSize : Int
)
