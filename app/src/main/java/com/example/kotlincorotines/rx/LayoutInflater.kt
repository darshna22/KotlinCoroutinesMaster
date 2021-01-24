package com.example.kotlincorotines.rx

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.view.ContextThemeWrapper
import com.example.kotlincorotines.R

/**
 * Inflates a [layoutResId] with an [appTheme].
 *
 * Defaults to [R.style.NextAppTheme].
 */
fun LayoutInflater.inflateWithTheme(
  context: Context?,
  @LayoutRes layoutResId: Int,
  root: ViewGroup?,
  attachToRoot: Boolean = false,
  appTheme: Int = R.style.NextAppTheme
): View {
  val wrapper = ContextThemeWrapper(context, appTheme)
  return this.cloneInContext(wrapper)
      .inflate(layoutResId, root, attachToRoot)
}
