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
    val calories : Int,
    val servingSize : Int
)