package com.example.kotlincorotines.di

import com.example.kotlincorotines.coroutines_listener.CoroutineApiService
import dagger.Module
import dagger.Provides
import dagger.Reusable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object NetworkModule {

  @Provides
  @Singleton
  internal fun provideRetrofit(): Retrofit =
      Retrofit.Builder()
          .baseUrl(NetworkUrl.BASE_URL)
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .addConverterFactory(GsonConverterFactory.create())
          .build()

  @Reusable
  @Provides
  @JvmStatic
  internal fun provideMarvelAPI(retrofit: Retrofit): MarvelApi =
      retrofit.create(MarvelApi::class.java)

  @Reusable
  @Provides
  @JvmStatic
  internal fun provideCoroutineApiService(retrofit: Retrofit): CoroutineApiService =
      retrofit.create(CoroutineApiService::class.java)

}