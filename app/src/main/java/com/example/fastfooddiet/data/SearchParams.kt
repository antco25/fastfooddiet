package com.example.fastfooddiet.data

import android.os.Parcel
import android.os.Parcelable

data class SearchParams(
    val query: String? = "",
    val restaurants: List<String>?,
    val foodType: List<String>?,
    val favorites: Int = 0,
    val caloriesMin: Int = 0,
    val caloriesMax: Int = -1,
    val proteinMin: Int = 0,
    val proteinMax: Int = -1,
    val fatMin: Int = 0,
    val fatMax: Int = -1,
    val sfatMin: Int = 0,
    val sfatMax: Int = -1,
    val tfatMin: Int = 0,
    val tfatMax: Int = -1,
    val cholMin: Int = 0,
    val cholMax: Int = -1,
    val sodiumMin: Int = 0,
    val sodiumMax: Int = -1,
    val carbMin: Int = 0,
    val carbMax: Int = -1,
    val sugarMin: Int = 0,
    val sugarMax: Int = -1,
    val fiberMin: Int = 0,
    val fiberMax: Int = -1
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<SearchParams> {
            override fun createFromParcel(parcel: Parcel) = SearchParams(parcel)
            override fun newArray(size: Int) = arrayOfNulls<SearchParams>(size)
        }
    }

    private constructor(parcel: Parcel) : this(
        query = parcel.readString(),
        restaurants = parcel.createStringArray()?.toList(),
        foodType = parcel.createStringArray()?.toList(),
        favorites = parcel.readInt(),
        caloriesMin = parcel.readInt(),
        caloriesMax = parcel.readInt(),
        proteinMin = parcel.readInt(),
        proteinMax = parcel.readInt(),
        fatMin = parcel.readInt(),
        fatMax = parcel.readInt(),
        sfatMin = parcel.readInt(),
        sfatMax = parcel.readInt(),
        tfatMin = parcel.readInt(),
        tfatMax = parcel.readInt(),
        cholMin = parcel.readInt(),
        cholMax = parcel.readInt(),
        sodiumMin = parcel.readInt(),
        sodiumMax = parcel.readInt(),
        carbMin = parcel.readInt(),
        carbMax = parcel.readInt(),
        sugarMin = parcel.readInt(),
        sugarMax = parcel.readInt(),
        fiberMin = parcel.readInt(),
        fiberMax = parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(query)
        parcel.writeStringArray(restaurants?.toTypedArray())
        parcel.writeStringArray(foodType?.toTypedArray())
        parcel.writeInt(favorites)
        parcel.writeInt(caloriesMin)
        parcel.writeInt(caloriesMax)
        parcel.writeInt(proteinMin)
        parcel.writeInt(proteinMax)
        parcel.writeInt(fatMin)
        parcel.writeInt(fatMax)
        parcel.writeInt(sfatMin)
        parcel.writeInt(sfatMax)
        parcel.writeInt(tfatMin)
        parcel.writeInt(tfatMax)
        parcel.writeInt(cholMin)
        parcel.writeInt(cholMax)
        parcel.writeInt(sodiumMin)
        parcel.writeInt(sodiumMax)
        parcel.writeInt(carbMin)
        parcel.writeInt(carbMax)
        parcel.writeInt(sugarMin)
        parcel.writeInt(sugarMax)
        parcel.writeInt(fiberMin)
        parcel.writeInt(fiberMax)
    }

    override fun describeContents() = 0
}