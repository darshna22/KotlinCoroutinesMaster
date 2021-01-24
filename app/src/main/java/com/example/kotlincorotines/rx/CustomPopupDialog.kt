package com.example.kotlincorotines.rx

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import com.example.kotlincorotines.R
import kotlinx.android.synthetic.main.layout_alert_popup_dialog.*

/**
 * A reusable alert dialog that appears from the bottom of the screen. The
 * dialog's title, message, hero image can be configured via the arguments
 * bundle.
 *
 * You may optionally set a callback to be invoked when the dialog is dismissed
 * using [setOnDismissListener].
 */
class CustomPopupDialog : NextBottomSheetDialogFragment(disablePeek = true) {

  private var onDismissListener: (() -> Unit)? = null

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View? {
    return inflater.inflateWithTheme(context, R.layout.layout_alert_popup_dialog, container)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupView()
  }

  private fun setupView() {
    arguments?.getString(ARGS_TITLE)?.let { title ->
      tvTitle.text = title
    }

    arguments?.getString(ARGS_MESSAGE)?.let { message ->
      tvMessage.text = message
    }

    arguments?.getInt(ARGS_HERO_IMAGE)?.let { heroImage ->
      ivHero.setImageResource(heroImage)
    }

    arguments?.getString(ARGS_BUTTON_TEXT)?.let { text ->
      btnPrimary.text = text
    }

    arguments?.getString(ARGS_FOOTER)?.let { footer ->
      tvFooter.visibility=View.VISIBLE
      tvFooter.text = footer
    }

    btnPrimary.setOnClickListener(this::onPrimaryButtonClick)
  }

  /**
   * Set a [listener] to be invoked when the dialog is dismissed.
   */
  fun setOnDismissListener(listener: () -> Unit) {
    onDismissListener = listener
  }

  private fun onPrimaryButtonClick(v: View) {
    this.dismiss()
  }

  override fun onDismiss(dialog: DialogInterface) {
    super.onDismiss(dialog)
    onDismissListener?.invoke()
  }

  companion object {

    /**
     * Returns a new instance of [CustomPopupDialog].
     */
    fun newInstance(
      title: String,
      message: String,
      buttonText: String,
      @DrawableRes heroImage: Int,
      onDismiss: (() -> Unit)? = null,
      footer: String? = null
    ): CustomPopupDialog {
      val bundle = Bundle().apply {
        putString(ARGS_TITLE, title)
        putString(ARGS_MESSAGE, message)
        putString(ARGS_BUTTON_TEXT, buttonText)
        putInt(ARGS_HERO_IMAGE, heroImage)
        footer?.let { putString(ARGS_FOOTER, footer) }
      }

      val dialog = CustomPopupDialog().apply {
        arguments = bundle
      }

      if (onDismiss !== null) {
        dialog.setOnDismissListener(onDismiss)
      }

      return dialog
    }

    private const val ARGS_TITLE = "ARGS_TITLE"
    private const val ARGS_MESSAGE = "ARGS_MESSAGE"
    private const val ARGS_BUTTON_TEXT = "ARGS_BUTTON_TEXT"
    private const val ARGS_HERO_IMAGE = "ARGS_HERO_IMAGE"
    private const val ARGS_FOOTER = "ARGS_FOOTER"
  }

}
