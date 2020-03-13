package com.example.fastfooddiet.data

import android.os.Parcel
import android.os.Parcelable

data class SearchParams(
    val query : String? = "",
    val restaurants : List<String>?,
    val foodType : List<String>?,
    val favorites : Int = 0,
    val caloriesMin : Int = -1,
    val caloriesMax : Int = -1,
    val servingSizeMin : Int = -1,
    val servingSizeMax : Int = -1
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<SearchParams> {
            override fun createFromParcel(parcel: Parcel) = SearchParams(parcel)
            override fun newArray(size: Int) = arrayOfNulls<SearchParams>(size)
        }
    }

    private constructor(parcel : Parcel) : this (
        query = parcel.readString(),
        restaurants = parcel.createStringArray()?.toList(),
        foodType = parcel.createStringArray()?.toList(),
        favorites = parcel.readInt(),
        caloriesMin = parcel.readInt(),
        caloriesMax = parcel.readInt(),
        servingSizeMin = parcel.readInt(),
        servingSizeMax = parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(query)
        parcel.writeStringArray(restaurants?.toTypedArray())
        parcel.writeStringArray(foodType?.toTypedArray())
        parcel.writeInt(favorites)
        parcel.writeInt(caloriesMin)
        parcel.writeInt(caloriesMax)
        parcel.writeInt(servingSizeMin)
        parcel.writeInt(servingSizeMax)
    }

    override fun describeContents() = 0
}