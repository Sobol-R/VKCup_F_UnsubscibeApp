package com.sobol.f_unsubscribe_app.model

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONObject

data class Group(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val photo: String = "",
    val membersCount: Int = 0,
    val screenName: String = "",
    var checked: Boolean = false
): Parcelable  {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(name)
        dest.writeString(description)
        dest.writeString(photo)
        dest.writeInt(membersCount)
        dest.writeString(screenName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Group> {
        override fun createFromParcel(parcel: Parcel): Group {
            return Group(parcel)
        }

        override fun newArray(size: Int): Array<Group?> {
            return arrayOfNulls(size)
        }

        fun parse(json: JSONObject) = Group(
            id = json.optInt("id", 0),
            name = json.optString("name", ""),
            description = json.optString("description", ""),
            photo = json.optString("photo_200", ""),
            membersCount = json.optInt("members_count", 0),
            screenName = json.optString("screen_name", "")
        )
    }


}