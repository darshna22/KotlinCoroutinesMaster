package com.example.kotlincorotines.di

import com.example.kotlincorotines.di.base.BaseUseCase
import com.example.kotlincorotines.di.entities.EmployeeResponse
import io.reactivex.Observable
import okhttp3.ResponseBody
import javax.inject.Inject

class GetMarvelUseCase @Inject constructor(
  private val api: MarvelApi
) : BaseUseCase.WithoutParams<ResponseBody>() {
  override fun onExecute(): Observable<ResponseBody> {
    return api.getMarvelHeros()
  }
}