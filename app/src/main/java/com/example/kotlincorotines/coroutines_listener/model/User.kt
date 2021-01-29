package com.example.kotlincorotines.coroutines_listener.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
  @SerializedName("name")
  var name: String,
  @SerializedName("email")
  var email: String
) : Parcelable {
  constructor() : this("", "")
}