package com.example.kotlincorotines.rx

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.core.util.Supplier
import com.example.kotlincorotines.R
import kotlinx.android.synthetic.main.layout_loading_overlay.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.threeten.bp.Clock
import th.co.ktb.next.libs.i18n.T
import kotlin.coroutines.CoroutineContext
import kotlin.properties.Delegates

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class LoadingOverlayDialog(
  /**
   * If set to `true`, default loading labels if they're not explicitly
   * provided.
   */
  private val useDefaultLabels: Boolean = true,

  /**
   * The duration in milliseconds each label will bse displayed.
   */
  private val labelCycleIntervalMillis: Long = DEFAULT_LABEL_CYCLE_INTERVAL_MILLIS,

  /**s
   * The minimum amount of time that the loading overlay must be shown before
   * being allowed to dismiss.
   */
  private val minimumShowTimeMillis: Long = DEFAULT_MINIMUM_SHOW_TIME_MILLIS,

  /**
   * The duration in milliseconds that the loading state must be persisted
   * before it is visible to the user. This helps to prevent flickering/flashing
   * for very short loading states.
   */
  private val delayBeforeVisibleMillis: Long = DEFAULT_DELAY_BEFORE_VISIBLE_MILLIS,
  private val clock: Clock = Clock.systemDefaultZone(),
  private val context: Context,
  override val coroutineContext: CoroutineContext
) : CoroutineScope {

  private val dialog: Dialog by lazy {
    val dialog = Dialog(context, R.style.NextLoadingOverlay)
    dialog.setCancelable(false)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(R.layout.layout_loading_overlay)
    setupWindow(dialog.window)
    return@lazy dialog
  }

  private var displayUiJob by Delegates.observable<Job?>(null) { _, oldJob, _ ->
    oldJob?.cancel()
  }

  private var cancelDialogJob by Delegates.observable<Job?>(null) { _, oldJob, _ ->
    oldJob?.cancel()
  }

  private var labelCyclerJob by Delegates.observable<Job?>(null) { _, oldJob, _ ->
    oldJob?.cancel()
  }

  private var lastShowTimestamp: Long? = null

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  private fun setupWindow(window: Window?) {
    if (window == null) return

    // Configure status bar
    window.statusBarColor = Color.TRANSPARENT
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.decorView.systemUiVisibility =
      View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

    // Configure background overlay
    window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    window.setBackgroundDrawableResource(android.R.color.transparent)

    // Configure size
    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
  }

  /**
   * Overlays the screen with a loading spinner with optional [labels].
   */
  fun show(labels: List<String>) {
    try {
      cancelDialogJob?.cancel()
    } catch (t: Throwable) { }

    if (dialog.isShowing) {
      return
    }

    // Clear all pending jobs
    displayUiJob?.cancel()
    labelCyclerJob?.cancel()

    // We want to block user input immediately when this function is called,
    // but only show the loading overlay after the loading state is persisted
    // for a certain amount of time. This prevents the loading overlay from
    // flashing/flickering for loading states that resolves in a short time.
    dialog.show()
    displayUiJob = launch {
      delay(delayBeforeVisibleMillis)
      lastShowTimestamp = clock.millis()
      showUiElements(labels)
    }
  }

  /**
   * Dismisses the loading overlay.
   */
  fun dismiss() {
    if (!dialog.isShowing) {
      return
    }

    // We also want dialogs to show for at least a certain minimum amount of
    // time before dismissing for better user experience.
    val showDuration = lastShowTimestamp?.let { clock.millis() - it }
    if (showDuration != null && showDuration < minimumShowTimeMillis) {
      cancelDialogJob = launch {
        delay(minimumShowTimeMillis - showDuration)
        dismiss()
      }
      return
    }

    hideUiElements()
    dialog.dismiss()
    displayUiJob?.cancel()
    labelCyclerJob?.cancel()
  }

  private fun showUiElements(labels: List<String>) {
    dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    dialog.divLoader.visibility=View.VISIBLE
    dialog.divLoader.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in_300))

    val loadingLabels = if (useDefaultLabels && labels.isEmpty()) {
      DEFAULT_LABELS.get()
    } else {
      labels
    }

    when {
      loadingLabels.isEmpty() -> setLabel(label = "", animate = false)
      loadingLabels.size == 1 -> setLabel(label = loadingLabels[0], animate = false)
      else -> startLabelCycler(
        labels = loadingLabels,
        interval = labelCycleIntervalMillis
      )
    }
  }

  private fun hideUiElements() {
    dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    if (dialog.divLoader.visibility == View.VISIBLE) {
      dialog.divLoader.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out_300))
    }
    dialog.divLoader.visibility=View.INVISIBLE
  }

  // LABELS

  /**
   * Begins cycling through the list of [labels] at a specific [interval] in
   * milliseconds.
   */
  private fun startLabelCycler(labels: List<String>, interval: Long) {
    labelCyclerJob = launch {
      for ((i, label) in labels.withIndex()) {
        setLabel(label, animate = i != 0) // Don't animate first label
        delay(interval)
      }
    }
  }

  /**
   * Sets the label in the loading overlay. Set [animate] to true to apply a
   * fade animation.
   */
  private fun setLabel(label: String, animate: Boolean = false) {
    if (!animate) {
      dialog.tvLabel.text = label
      return
    }

    val animateIn = AnimationUtils.loadAnimation(context, R.anim.fade_in_300)
    val animateOut = AnimationUtils.loadAnimation(context, R.anim.fade_out_300).apply {
//      setAnimationListener(onEnd = {
//        dialog.tvLabel.text = label
//        dialog.tvLabel.startAnimation(animateIn)
//      })
    }

    dialog.tvLabel.startAnimation(animateOut)
  }

  companion object {
    private const val DEFAULT_DELAY_BEFORE_VISIBLE_MILLIS = 300L
    private const val DEFAULT_MINIMUM_SHOW_TIME_MILLIS = 500L
    private const val DEFAULT_LABEL_CYCLE_INTERVAL_MILLIS = 2_000L
    private val DEFAULT_LABELS = Supplier {
      listOf(T.get("loading_default_message"), T.get("loading_default_message_extended"))
    }
  }

}
