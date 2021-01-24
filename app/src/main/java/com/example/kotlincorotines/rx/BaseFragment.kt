package com.example.kotlincorotines.rx

import android.util.Log
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class BaseFragment : Fragment(), CoroutineScope, Disposer {

  override val disposeBag by lazy { CompositeDisposable() }

  protected val job = Job()

  override val coroutineContext = job + Dispatchers.Main

  override fun onDestroy() {
    super.onDestroy()
    job.cancel()
    disposeBag.dispose()
  }

  /**
   * Set window soft input mode for the screen.
   */

  protected fun setWindowSoftInputMode(mode: Int) {
    (this.activity as? BaseActivity)?.setWindowSoftInputMode(mode)
  }

  /**
   * Displays a fullscreen loading overlay with [labels].
   */
  protected fun showLoading(vararg labels: String) {
    val activity = this.activity
    if (activity is BaseActivity) {
      activity.showLoading(*labels)
    } else {
      Log.w(TAG, "Unable to show loading overlay. Parent activity is not of type BaseActivity.")
    }
  }

  /**
   * Dismisses the fullscreen loading overlay.
   */
  protected fun dismissLoading() {
    val activity = this.activity
    if (activity is BaseActivity) {
      activity.dismissLoading()
    } else {
      Log.w(TAG, "Unable to dismiss loading overlay. Parent activity is not of type BaseActivity.")
    }
  }

  // ALERT HANDLING

  /**
   * Displays a toast alert.
   */
  fun showToast(alert: Alert.Toast) {
    (this.activity as? BaseActivity)?.showToast(alert)
  }

  /**
   * Displays a native popup alert.
   */
  fun showNativePopupAlert(alert: Alert.NativePopup) {
    (this.activity as? BaseActivity)?.showNativePopupAlert(alert)
  }

  /**
   * Displays a custom popup alert.
   */
  fun showCustomPopupAlert(alert: Alert.CustomPopup) {
    (this.activity as? BaseActivity)?.showCustomPopupAlert(alert)
  }

  /**
   * Displays a web browser dialog.
   */
  fun showWebBrowserDialog(alert: Alert.WebBrowser) {
    (this.activity as? BaseActivity)?.showWebBrowserDialog(alert)
  }

  companion object {
    private const val TAG = "BaseFragment"
  }

}
