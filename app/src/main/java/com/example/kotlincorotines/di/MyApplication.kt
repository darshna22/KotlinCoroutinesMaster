/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.kotlincorotines.di

import android.app.Application

open class MyApplication : Application() {

  // Instance of the AppComponent that will be used by all the Activities in the project
  val appComponent: AppComponent by lazy {
    // Creates an instance of AppComponent using its Factory constructor
    // We pass the applicationContext that will be used as Context in the graph
    DaggerAppComponent.factory().create(applicationContext)
  }

//    open val userManager by lazy {
//        UserManager(SharedPreferencesStorage(this))
//    }
}
