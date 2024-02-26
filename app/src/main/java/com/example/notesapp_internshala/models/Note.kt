package com.example.notesapp_internshala.models

import android.os.Parcel
import android.os.Parcelable

data class Note(
    val currentUserId: String = "",
    val title: String = "",
    val description: String = "",
    var documentId: String = "",
    val timestamp: Long = System.currentTimeMillis()
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(documentId)
    }

    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }
}
