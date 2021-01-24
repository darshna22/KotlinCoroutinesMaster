package com.example.kotlincorotines.di

import android.content.Context
import com.example.kotlincorotines.MainActivity
import com.example.kotlincorotines.MyTestingActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

// Definition of a Dagger component that adds info from the StorageModule to the graph
@Singleton
@Component(modules = [NetworkModule::class])
interface AppComponent {

  // Factory to create instances of the AppComponent
  @Component.Factory
  interface Factory {
    // With @BindsInstance, the Context passed in will be available in the graph
    fun create(@BindsInstance context: Context): AppComponent
  }

  fun inject(activity: MainActivity)
  fun inject(activity: MyTestingActivity)

}