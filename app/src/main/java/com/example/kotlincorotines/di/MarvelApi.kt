package com.example.kotlincorotines.di

import com.example.kotlincorotines.di.entities.EmployeeResponse
import com.example.kotlincorotines.di.entities.MarvelHerosResponseX
import io.reactivex.Observable
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.GET

interface MarvelApi {

  @GET("v1/employees")
  fun getTypeCode(): Observable<EmployeeResponse>

  @GET("/")
  fun getMarvelHeros(): Observable<ResponseBody>
}