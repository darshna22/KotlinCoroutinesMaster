package com.example.kotlincorotines.rx

import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.EditText
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class NextBottomSheetDialogFragment(

  /**
   * Disables the `peeking` state of the bottom sheet dialog.
   */
  private val disablePeek: Boolean = false,

  /**
   * Forces the bottom sheet dialog to renders its children in a full height
   * container.
   */
  private val forceFullHeight: Boolean = false,

  /**
   * Disables the dragging behaviour of the dialog.
   */
  private val disableDrag: Boolean = false,

  /**
   * Clears the focus of the current view upon showing this dialog.
   */
  private val clearFocusOnShow: Boolean = false

) : BottomSheetDialogFragment() {

  private val bottomSheetBehavior: BottomSheetBehavior<View>?
    get() {
      val parent = dialog?.let { getContainerLayout(it) }
      return parent?.let { BottomSheetBehavior.from(it) }
    }

  private val bottomSheetCallback = NextBottomSheetCallback()

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val dialog = super.onCreateDialog(savedInstanceState)

    if (clearFocusOnShow) {
      // Typically when a [BottomSheetDialogFragment] is launched when a keyboard
      // is visible, the keyboard is dismissed. However this dismissal is not
      // notified to the currently focused view. This causes potential issues if
      // that view depends on this behaviour (eg. validation).
      //
      // To fix this, we are manually simulating a "close keyboard" event here.
      when (val view = this.activity?.currentFocus) {
        is EditText -> view.dispatchKeyEventPreIme(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK))
      }
    }

    dialog.setOnShowListener {
      bottomSheetBehavior?.setBottomSheetCallback(bottomSheetCallback)

      if (disablePeek) {
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetBehavior?.skipCollapsed = true
        bottomSheetBehavior?.isHideable = true
      }

      if (disableDrag) {
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetBehavior?.isHideable = false

        // Even if `isHideable` is set to `false`, the dialog will still be
        // draggable to the peek height. We can fix this by making the peek
        // height itself the screen height.
        val displayMetrics = DisplayMetrics().apply {
          activity?.windowManager?.defaultDisplay?.getMetrics(this)
        }
        bottomSheetBehavior?.peekHeight = displayMetrics.heightPixels
      }
    }

    return dialog
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    if (forceFullHeight) forceFullHeight(view)
  }

  private fun forceFullHeight(view: View) {
    val observer = object : ViewTreeObserver.OnGlobalLayoutListener {
      override fun onGlobalLayout() {
        val parent = getContainerLayout(dialog)
        val layoutParams = parent?.layoutParams ?: return

        // By default, the parent's height is set to WRAP_CONTENT, which is why
        // child layouts do not expand to full height
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        parent.layoutParams = layoutParams

        // We have to remove this listener after the first call, otherwise this
        // will be invoked repeatedly causing the app to stutter
        view.viewTreeObserver.removeOnGlobalLayoutListener(this)
      }
    }
    view.viewTreeObserver.addOnGlobalLayoutListener(observer)
  }

  /**
   * Returns the dialog's containing parent.
   */
  private fun getContainerLayout(dialog: Dialog?): FrameLayout? {
    val id = com.google.android.material.R.id.design_bottom_sheet
    return dialog?.findViewById(id)
  }

  private inner class NextBottomSheetCallback : BottomSheetBehavior.BottomSheetCallback() {
    override fun onStateChanged(v: View, state: Int) {
      if (state == BottomSheetBehavior.STATE_HIDDEN) {
        dismiss()
      }

      if (disablePeek) {
        // We need to force the layout to re-render, otherwise layouts with
        // dynamic height might be positioned incorrectly.
        // eg. while keyboard is open, launch a calendar picker dialog.
        //     without this, the dialog will be stuck at the top.
        //
        // This is a bug in the BottomSheetBehavior itself:
        // @see https://issuetracker.google.com/issues/37090839
        v.post {
          v.requestLayout()
          v.invalidate()
        }
      }
    }

    override fun onSlide(v: View, offset: Float) {
      // do nothing
    }
  }

}
