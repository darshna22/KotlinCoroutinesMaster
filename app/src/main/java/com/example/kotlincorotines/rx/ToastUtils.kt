package com.example.kotlincorotines.rx

import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.kotlincorotines.R
import kotlinx.android.synthetic.main.layout_next_toast.view.*
import kotlin.math.roundToInt

class ToastUtils {

  companion object {

    private lateinit var layoutInflater: LayoutInflater
    fun showSuccess(context: AppCompatActivity, message: String) {
      layoutInflater = LayoutInflater.from(context)
      val layout = layoutInflater.inflate(
          R.layout.layout_next_toast,
          (context).findViewById(R.id.divToastRootLayout)
      )
      layout.ivToastImage.setImageDrawable(
          ContextCompat.getDrawable(
              context,
              R.drawable.ic_check_filled
          )
      )
      val drawable = ContextCompat.getDrawable(context, R.drawable.bg_toast_success)
      layout.divToastChildLayout.background = drawable
      layout.tvToastMessage.text = message
      val toast = Toast(context.applicationContext)
      toast.duration = Toast.LENGTH_SHORT
      val gravity = Gravity.TOP or Gravity.FILL_HORIZONTAL
      val horizontalOffset = 0
      val verticalOffset =  context.resources.getDimension(R.dimen.spacing_tight)
      toast.setGravity(gravity, horizontalOffset, verticalOffset.roundToInt())
      toast.view = layout
      toast.show()
    }

    fun showWarning(context: AppCompatActivity, message: String) {
      layoutInflater = LayoutInflater.from(context)
      val layout = layoutInflater.inflate(
          R.layout.layout_next_toast,
          (context).findViewById(R.id.divToastRootLayout)
      )
      layout.ivToastImage.setImageDrawable(
          ContextCompat.getDrawable(
              context,
              R.drawable.ic_check_filled
          )
      )
      val drawable = ContextCompat.getDrawable(context, R.drawable.bg_toast_warning)
      layout.divToastChildLayout.background = drawable
      layout.tvToastMessage.text = message
      val toast = Toast(context.applicationContext)
      toast.duration = Toast.LENGTH_SHORT
      val gravity = Gravity.TOP or Gravity.FILL_HORIZONTAL
      val horizontalOffset = 0
      val verticalOffset = context.resources.getDimension(R.dimen.spacing_tight)
      toast.setGravity(gravity, horizontalOffset, verticalOffset.roundToInt())
      toast.view = layout
      toast.show()
    }

    fun showError(context: AppCompatActivity, message: String) {
      layoutInflater = LayoutInflater.from(context)
      val layout = layoutInflater.inflate(
          R.layout.layout_next_toast,
          (context).findViewById(R.id.divToastRootLayout)
      )
      layout.ivToastImage.setImageDrawable(
          ContextCompat.getDrawable(
              context,
              R.drawable.ic_error_filled
          )
      )
      val drawable = ContextCompat.getDrawable(context, R.drawable.bg_toast_error)
      layout.divToastChildLayout.background = drawable
      layout.tvToastMessage.text = message
      val toast = Toast(context.applicationContext)
      toast.duration = Toast.LENGTH_SHORT
      val gravity = Gravity.TOP or Gravity.FILL_HORIZONTAL
      val horizontalOffset = 0
      val verticalOffset =  context.resources.getDimension(R.dimen.spacing_tight)
      toast.setGravity(gravity, horizontalOffset, verticalOffset.roundToInt())
      toast.view = layout
      toast.show()
    }
  }
}
