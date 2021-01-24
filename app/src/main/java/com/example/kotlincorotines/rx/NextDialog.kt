package com.example.kotlincorotines.rx

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kotlincorotines.R
import kotlinx.android.synthetic.main.layout_next_dialog.*

class NextDialog : DialogFragment() {

  private lateinit var title: String
  private var message: String? = null
  private lateinit var positiveButtonLabel: String
  private lateinit var negativeButtonLabel: String
  private var onPositiveButtonClickListener: (() -> Unit)? = null
  private var onNegativeButtonClickListener: (() -> Unit)? = null

  private var userSelection = Selection.NEGATIVE

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View? {
    return inflater.inflateWithTheme(this.requireContext(), R.layout.layout_next_dialog, container)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initArguments()
    dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

    tvTitle.text = title

    if (message.isNullOrBlank()) {
      tvMessage.visibility=View.GONE
    } else {
      tvMessage.text = message
    }

    if (positiveButtonLabel.isNotEmpty()) {
      btnPositive.visibility = View.VISIBLE
      btnPositive.text = positiveButtonLabel
      btnPositive.setOnClickListener(this::onPositiveButtonClick)
    }

    if (negativeButtonLabel.isNotEmpty()) {
      btnNegative.visibility = View.VISIBLE
      btnNegative.text = negativeButtonLabel
      btnNegative.setOnClickListener(this::onNegativeButtonClick)
    }
  }

  private fun initArguments() {
    arguments?.let {
      title = it.getString(ARGS_TITLE) ?: throw IllegalStateException()
      message = it.getString(ARGS_MESSAGE)
      positiveButtonLabel = it.getString(ARGS_POSITIVE_BTN_LABEL) ?: throw IllegalStateException()
      negativeButtonLabel = it.getString(ARGS_NEGATIVE_BTN_LABEL) ?: throw IllegalStateException()
    }
  }

  override fun onDismiss(dialog: DialogInterface) {
    when (userSelection) {
      Selection.NEGATIVE -> onNegativeButtonClickListener?.invoke()
      Selection.POSITIVE -> onPositiveButtonClickListener?.invoke()
    }
    super.onDismiss(dialog)
  }

  private fun onPositiveButtonClick(v: View) {
    userSelection = Selection.POSITIVE
    this.dismiss()
  }

  private fun onNegativeButtonClick(v: View) {
    userSelection = Selection.NEGATIVE
    this.dismiss()
  }

  fun setPositiveButtonClickListener(onClick: (() -> Unit)? = null) {
    this.onPositiveButtonClickListener = onClick
  }

  fun setNegativeButtonClickListener(onClick: (() -> Unit)? = null) {
    this.onNegativeButtonClickListener = onClick
  }

  companion object {
    private const val ARGS_TITLE = "ARGS_TITLE"
    private const val ARGS_MESSAGE = "ARGS_MESSAGE"
    private const val ARGS_POSITIVE_BTN_LABEL = "ARGS_POSITIVE_BTN_LABEL"
    private const val ARGS_NEGATIVE_BTN_LABEL = "ARGS_NEGATIVE_BTN_LABEL"
  }

  data class Builder(
    var title: String? = null,
    var message: String? = null,
    var positiveButtonText: String? = null,
    var onPositiveButtonClick: (() -> Unit)? = null,
    var negativeButtonText: String? = null,
    var onNegativeButtonClick: (() -> Unit)? = null
  ) {

    fun title(title: String) = apply {
      this.title = title
    }

    fun message(message: String?) = apply {
      this.message = message
    }

    fun positiveButton(text: String, onClick: (() -> Unit)? = null) = apply {
      this.positiveButtonText = text
      this.onPositiveButtonClick = onClick
    }

    fun negativeButton(text: String, onClick: (() -> Unit)? = null) = apply {
      this.negativeButtonText = text
      this.onNegativeButtonClick = onClick
    }

    fun build(): NextDialog {
      val arguments = Bundle().apply {
        putString(ARGS_TITLE, title)
        message?.let { putString(ARGS_MESSAGE, it) }
        putString(ARGS_POSITIVE_BTN_LABEL, positiveButtonText ?: "")
        putString(ARGS_NEGATIVE_BTN_LABEL, negativeButtonText ?: "")
      }

      val dialog = NextDialog().also { it.arguments = arguments }

      positiveButtonText?.let {
        dialog.setPositiveButtonClickListener(onPositiveButtonClick)
      }

      negativeButtonText?.let {
        dialog.setNegativeButtonClickListener(onNegativeButtonClick)
      }

      return dialog
    }
  }

  enum class Selection { POSITIVE, NEGATIVE }

}
