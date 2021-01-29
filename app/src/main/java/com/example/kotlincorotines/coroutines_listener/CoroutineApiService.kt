package com.example.kotlincorotines.coroutines_listener

import com.example.kotlincorotines.coroutines_listener.model.JokeListResponse
import retrofit2.Response
import retrofit2.http.GET

interface CoroutineApiService {
  @GET("/jokes/random/5")
  suspend fun getFiveRandomJokes(): Response<JokeListResponse>
}