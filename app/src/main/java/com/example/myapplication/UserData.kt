// Friend.kt
package com.example.myapplication

import android.os.Parcel
import android.os.Parcelable

data class Friend(
    val profilePicture: String?,
    val name: String?,
    val birthday: String? = null,
    val nickname: String,
    val biggestDream: String,
    val currentMoodSong: String,
    val motivation: String,
    val hiddenTalent: String,
    val favoriteQuote: String,
    val threeThings: String,
    val celebrityCrush: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(profilePicture)
        parcel.writeString(name)
        parcel.writeString(birthday)
        parcel.writeString(nickname)
        parcel.writeString(biggestDream)
        parcel.writeString(currentMoodSong)
        parcel.writeString(motivation)
        parcel.writeString(hiddenTalent)
        parcel.writeString(favoriteQuote)
        parcel.writeString(threeThings)
        parcel.writeString(celebrityCrush)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Friend> {
        override fun createFromParcel(parcel: Parcel): Friend {
            return Friend(parcel)
        }

        override fun newArray(size: Int): Array<Friend?> {
            return arrayOfNulls(size)
        }
    }
}
