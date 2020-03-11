package br.com.calcard.android.app.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class Beneficiary(
        var name: String?,
        var percentage: Long?
) : Serializable, Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readValue(Long::class.java.classLoader) as? Long) {
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(name)
        dest?.writeValue(percentage)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<Beneficiary> {
        override fun createFromParcel(parcel: Parcel): Beneficiary {
            return Beneficiary(parcel)
        }

        override fun newArray(size: Int): Array<Beneficiary?> {
            return arrayOfNulls(size)
        }
    }
}