package com.example.kotlincorotines.coroutines_listener.repo

import com.example.kotlincorotines.coroutines_listener.CoroutineApiService
import com.example.kotlincorotines.coroutines_listener.NetworkResult
import com.example.kotlincorotines.coroutines_listener.model.JokeListResponse
import javax.inject.Inject

class JokeRepo @Inject constructor(private val coroutineApiService: CoroutineApiService) {

  suspend fun getFiveRandomJokes(): NetworkResult<JokeListResponse> {
    val response = coroutineApiService.getFiveRandomJokes()
    if (response.isSuccessful) {
      val data = response.body()
      if (data != null) {
        return NetworkResult.Success(data)
      }
    }
    return NetworkResult.Failure(response)
  }
}