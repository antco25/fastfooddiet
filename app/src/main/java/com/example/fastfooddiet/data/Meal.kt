package com.example.fastfooddiet.data

import androidx.room.*

@Entity(tableName = "meal_table")
data class MealData (
    @PrimaryKey (autoGenerate = true)
    val mealId : Int = 0,
    val name : String
)

@Entity(tableName = "meal_food_table", indices = [Index(value = ["mealId"]), Index(value = ["foodId"])])
data class MealFoodCrossRef (
    @PrimaryKey (autoGenerate = true)
    val mealFoodId: Int = 0,
    val mealId : Int,
    val foodId : Int
)

data class Meal (
    @Embedded val mealData: MealData,
    @Relation(
        parentColumn = "mealId",
        entityColumn = "foodId",
        associateBy = Junction(MealFoodCrossRef::class)
    )
    val foods : List<Food>,
    @Relation(
        parentColumn = "mealId",
        entityColumn = "mealId"
    )
    val mealFoods : List<MealFoodCrossRef>

) : ListItem {
    override val itemName: String
        get() = mealData.name
    override val itemId: Int
        get() = mealData.mealId

}
