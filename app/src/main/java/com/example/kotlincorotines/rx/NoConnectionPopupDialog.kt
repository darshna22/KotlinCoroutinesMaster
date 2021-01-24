package com.example.kotlincorotines.rx

import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kotlincorotines.R
import kotlinx.android.synthetic.main.layout_alert_no_connection_dialog.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * A reusable alert dialog that appears from the bottom of the screen. The
 * dialog's title, message, hero image can be configured via the arguments
 * bundle.
 *
 * You may optionally set a callback to be invoked when the dialog is dismissed
 * using [setOnDismissListener].
 */
class NoConnectionPopupDialog : NextBottomSheetDialogFragment(disablePeek = true), CoroutineScope {

  private val job = Job()

  override val coroutineContext: CoroutineContext = job + Dispatchers.Main

  private var onDismissListener: (() -> Unit)? = null

  private val connectivityManager: ConnectivityManager by lazy {
    return@lazy context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View? {
    return inflater.inflateWithTheme(context, R.layout.layout_alert_no_connection_dialog, container)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    btnPrimary.setOnClickListener { onPrimaryButtonClick(it) }
  }

  override fun onDestroy() {
    super.onDestroy()
    job.cancel()
  }

  /**
   * Set a [listener] to be invoked when the dialog is dismissed.
   */
  fun setOnDismissListener(listener: () -> Unit) {
    onDismissListener = listener
  }

  private fun onPrimaryButtonClick(v: View) = launch {
    lsLoading.visibility=View.VISIBLE
    btnPrimary.visibility=View.INVISIBLE

    // Simulate an artificial delay for better UX
    delay(700)

    if (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo?.isConnected == true) {
      dismiss()
    } else {
      lsLoading.visibility=View.GONE
      btnPrimary.visibility=View.VISIBLE
    }
  }

  override fun onDismiss(dialog: DialogInterface) {
    super.onDismiss(dialog)
    onDismissListener?.invoke()
  }

  companion object {

    /**
     * Returns a new instance of [NoConnectionPopupDialog].
     */
    fun newInstance(
      onConnected: (() -> Unit)? = null
    ): NoConnectionPopupDialog {
      val bundle = Bundle().apply {
      }

      val dialog = NoConnectionPopupDialog().apply {
        arguments = bundle
      }

      if (onConnected !== null) {
        dialog.setOnDismissListener(onConnected)
      }

      return dialog
    }
  }

}
