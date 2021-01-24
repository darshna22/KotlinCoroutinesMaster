package com.example.kotlincorotines.rx

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NoticeScreenContext(
  val screenTitle: String,
  @DrawableRes val imageResId: Int? = null,
  val heading: String,
  val message: String,
  val buttonLabel: String? = null,
  val screenAnalyticsEvent: NextEvent? = null,
  val buttonAnalyticsEvent: NextEvent? = null,
  val footerMessage: String? = null
) : Parcelable
