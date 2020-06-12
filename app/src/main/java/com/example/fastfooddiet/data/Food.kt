package com.example.fastfooddiet.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_table")
data class Food (
    @PrimaryKey (autoGenerate = true)
    val foodId : Int,
    val name : String,
    val restaurant : String,
    val foodType : String,
    val servingSize : String,
    val calories : Int,
    val fat : Int,
    val satfat : Int,
    val transfat : Float,
    val chol : Int,
    val sodium : Int,
    val carbs : Int,
    val sugar : Int,
    val fiber : Int,
    val protein : Int,
    val sizeMode : Int,
    val size : String? = null,
    val favorite : Boolean = false
) : ListItem {
    override val itemName: String
        get() = name
    override val itemId: Int
        get() = foodId
    override fun description(showItemDetailWithSize : Boolean): String {
        return if (showItemDetailWithSize && size != null) "$restaurant - $foodType - $size"
        else "$restaurant - $foodType"
    }
}

/*
    Reference for 'sizeMode':
    0 - No other item sizes
    1 - Default item size
    2 - Non-default item size

    Ex: Coffee - Small (2), Medium (1), Large (2)
 */

data class FoodSize(
    val foodId: Int,
    val size : String? = null
)
