package com.example.fastfooddiet.data

import android.os.Parcel
import android.os.Parcelable

//TODO: SearchParams
data class SearchParams(
    val query : String? = "",
    val caloriesMin : Int = 0,
    val caloriesMax : Int = 0,
    val servingSizeMin : Int = 0,
    val servingSizeMax : Int = 0
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
        caloriesMin = parcel.readInt(),
        caloriesMax = parcel.readInt(),
        servingSizeMin = parcel.readInt(),
        servingSizeMax = parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(query)
        parcel.writeInt(caloriesMin)
        parcel.writeInt(caloriesMax)
        parcel.writeInt(servingSizeMin)
        parcel.writeInt(servingSizeMax)
    }

    override fun describeContents() = 0
}