package com.example.kotlincorotines.rx

data class LoadingState(
  val isLoading: Boolean,
  val labels: List<String> = listOf()
)
