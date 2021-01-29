package com.example.kotlincorotines.coroutines_listener.model

import com.example.kotlincorotines.coroutines_listener.model.Joke
import com.google.gson.annotations.SerializedName

data class JokeListResponse(
  @SerializedName("type")
  val type: String,
  @SerializedName("value")
  val value: ArrayList<out Joke>) {
}