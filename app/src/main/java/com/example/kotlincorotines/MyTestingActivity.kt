package com.example.kotlincorotines

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlincorotines.di.MyApplication
import com.example.kotlincorotines.ui.MarvelHeroViewModel
import javax.inject.Inject

class MyTestingActivity : AppCompatActivity() {

  // @Inject annotated fields will be provided by Dagger
  @Inject lateinit var marvelHeroViewModel: MarvelHeroViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    // Ask Dagger to inject our dependencies
    (application as MyApplication).appComponent.inject(this)

    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

  }

  override fun onResume() {
    super.onResume()
    marvelHeroViewModel.initialize()

  }
}