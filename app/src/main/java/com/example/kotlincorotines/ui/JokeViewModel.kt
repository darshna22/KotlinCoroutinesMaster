package com.example.kotlincorotines.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlincorotines.coroutines_listener.NetworkResult
import com.example.kotlincorotines.coroutines_listener.model.JokeListResponse
import com.example.kotlincorotines.coroutines_listener.repo.JokeRepo
import com.example.kotlincorotines.di.GetMarvelUseCase
import com.example.kotlincorotines.rx.BaseViewModel
import com.example.kotlincorotines.rx.disposedBy
import com.example.kotlincorotines.rx.doToggleLoadingStateOf
import com.example.kotlincorotines.rx.subscribeWithViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class JokeViewModel @Inject constructor(
  private val jokeRepo: JokeRepo
) : BaseViewModel() {
  var dataJokes: MutableLiveData<JokeListResponse> = MutableLiveData()

  fun initialize() {
    viewModelScope.launch {
    val jokeListResult= jokeRepo.getFiveRandomJokes()
      when (jokeListResult) {
        is NetworkResult.Success -> {
          dataJokes.value = jokeListResult.body
        }
        is NetworkResult.Failure -> {
          Timber.e("onError")
        }
      }
    }
  }


}