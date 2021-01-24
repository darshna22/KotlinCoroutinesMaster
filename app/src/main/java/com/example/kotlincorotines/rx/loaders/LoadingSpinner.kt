package com.example.kotlincorotines.rx.loaders

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.example.kotlincorotines.R
import kotlinx.android.synthetic.main.layout_loading_spinner.view.*

class LoadingSpinner @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyle: Int = 0,
  defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {

  init {
    View.inflate(context, R.layout.layout_loading_spinner, this)
  }

  init {
    // Pass down `android:orientation` attribute setting to inner child.
    setLoaderOrientation(this.orientation)
  }

  /**
   * Sets the loading spinner to [label].
   */
  fun setLabel(label: String) {
    tvLabel.text = label
  }

  private fun setLoaderOrientation(orientation: Int) {
    divLoader.orientation = orientation
    when (orientation) {
      VERTICAL -> tvLabel.setPadding(
          0,                                                                 // left
          context.resources.getDimension(R.dimen.spacing_tight).toInt(),     // top
          this.paddingRight,                                                 // right
          this.paddingBottom                                                 // bottom
      )
      HORIZONTAL -> tvLabel.setPadding(
          context.resources.getDimension(R.dimen.spacing_standard).toInt(), // left
          0,                                                                // top
          this.paddingRight,                                                // right
          this.paddingBottom                                                // bottom
      )
    }
  }

}
