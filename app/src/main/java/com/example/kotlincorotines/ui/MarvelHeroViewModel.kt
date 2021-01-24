package com.example.kotlincorotines.ui

import com.example.kotlincorotines.di.GetMarvelUseCase
import com.example.kotlincorotines.rx.BaseViewModel
import com.example.kotlincorotines.rx.disposedBy
import com.example.kotlincorotines.rx.doToggleLoadingStateOf
import com.example.kotlincorotines.rx.subscribeWithViewModel
import javax.inject.Inject

class MarvelHeroViewModel @Inject constructor(
  private val getMarvelUseCase: GetMarvelUseCase
) : BaseViewModel() {

  fun initialize() {
    getMarvelUseCase.build()
        .doToggleLoadingStateOf(this)
        .subscribeWithViewModel(vm = this,
          onNext = {
            print("darshna size $it")
          }, onError = {
            print("darshna error=" + it.message)
          })
        .disposedBy(this)
  }


}