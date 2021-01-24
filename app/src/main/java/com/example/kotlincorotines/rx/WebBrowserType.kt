package com.example.kotlincorotines.rx

import com.google.gson.annotations.SerializedName

enum class WebBrowserType {

  @SerializedName("INTERNAL")
  IN_APP_BROWSER,

  @SerializedName("EXTERNAL")
  EXTERNAL_BROWSER

}
