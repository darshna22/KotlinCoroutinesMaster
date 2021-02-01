package th.co.ktb.next.libs.animation

import android.view.animation.Interpolator
import kotlin.math.sqrt

class EaseOutCircInterpolator : Interpolator {

  override fun getInterpolation(value: Float): Float {
    var t = value
    t--
    return sqrt((1 - t * t).toDouble()).toFloat()
  }

}
