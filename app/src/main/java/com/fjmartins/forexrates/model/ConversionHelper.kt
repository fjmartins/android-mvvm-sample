package com.fjmartins.forexrates.model

import android.os.Parcel
import android.os.Parcelable

class ConversionHelper() : Parcelable {
    var currency: String? = ""
    var amount: Double = 0.0

    constructor(parcel: Parcel) : this() {
        currency = parcel.readString()
        amount = parcel.readDouble()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(currency)
        parcel.writeDouble(amount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ConversionHelper> {
        override fun createFromParcel(parcel: Parcel): ConversionHelper {
            return ConversionHelper(parcel)
        }

        override fun newArray(size: Int): Array<ConversionHelper?> {
            return arrayOfNulls(size)
        }
    }
}