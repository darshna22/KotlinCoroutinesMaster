package com.example.kotlincorotines.rx

import androidx.annotation.DrawableRes
import com.example.kotlincorotines.R
import th.co.ktb.next.libs.i18n.T
import java.io.InterruptedIOException
import java.net.SocketTimeoutException

sealed class Alert {

  /**
   * Displays a [Toast] alert.
   */
  data class Toast(
    val type: ToastType = ToastType.SUCCESS,
    val message: String
  ) : Alert()

  /**
   * Displays a native popup alert.
   */
  data class NativePopup(
    val title: String,
    val message: String?,
    val positiveButtonLabel: String,
    val positiveButtonCallback: (() -> Unit)? = null,
    val negativeButtonLabel: String?,
    val negativeButtonCallback: (() -> Unit)? = null
  ) : Alert() {

    constructor(
      title: String? = null,
      message: String? = null,
      positiveButton: NativePopupButton? = null,
      negativeButton: NativePopupButton? = null
    ) : this(
      title = title ?: T.get("common_default_error_title"),
      message = message,
      positiveButtonLabel = positiveButton?.label ?: T.get("common_button_ok"),
      positiveButtonCallback = positiveButton?.callback,
      negativeButtonLabel = negativeButton?.label,
      negativeButtonCallback = negativeButton?.callback
    )

  }

  /**
   * Displays a custom popup alert.
   */
  data class CustomPopup(
    val title: String,
    val message: String,
    @DrawableRes val heroImage: Int,
    val onDismiss: (() -> Unit)?,
    val buttonText: String,
    val footer: String? = null
  ) : Alert() {

    constructor(
      title: String? = null,
      message: String? = null,
      @DrawableRes heroImage: Int? = null,
      onDismiss: (() -> Unit)? = null,
      buttonText: String? = null,
      footer: String? = null
    ) : this(
      title = title ?: T.get("common_default_error_title"),
      message = message ?: T.get("common_default_error_message"),
      heroImage = heroImage ?: R.drawable.img_mobile_negative,
      onDismiss = onDismiss,
      buttonText = buttonText ?: T.get("common_button_ok"),
      footer = footer
    )

    companion object {
      /**
       * Creates a default [CustomPopup] to show based on a throwable [t].
       */
      fun fromThrowable(t: Throwable, onDismiss: (() -> Unit)? = null): CustomPopup {
        val footer = when (t) {
          is SocketTimeoutException, is InterruptedIOException -> T.get("common_error_code_label") + ": " + ApiException.ERROR_CODE_API_TIMEOUT
          else -> (t as? ApiException)?.error?.getAdditionalErrorInformation()
        }

        return CustomPopup(
          title = (t as? ApiException)?.error?.title.takeIfNotBlank(),
          message = (t as? ApiException)?.error?.message?.takeIfNotBlank(),
          footer = footer,
          onDismiss = onDismiss
        )
      }
    }
  }

  /**
   * Displays a custom alert view.
   */
  data class AlertView(
    val title: String,
    val message: String,
    @DrawableRes val heroImage: Int,
    val button: AlertButton?,
    @Deprecated("Configure NextEvent on the parent fragment instead.")
    val screenName: String?
  ) : Alert() {

    constructor(
      title: String? = null,
      message: String? = null,
      @DrawableRes heroImage: Int? = null,
      button: AlertButton? = null,
      screenName: String? = null
    ) : this(
      title = title ?: T.get("common_default_error_title"),
      message = message ?: T.get("common_default_error_message"),
      heroImage = heroImage ?: R.drawable.img_missing_accounts,
      button = button,
      screenName = screenName
    )
  }

  /**
   * Displays a web browser dialog.
   */
  class WebBrowser(
    val title: String,
    val url: String,
    val type: WebBrowserType = WebBrowserType.IN_APP_BROWSER,
    val header: HashMap<String, String>? = null,
    val onDismiss: (() -> Unit)?
  ) : Alert()

  /**
   * Displays a dialog to indicate no internet connectivity.
   */
  class NoConnectionPopup(
    val onConnected: () -> Unit
  ) : Alert()

}

data class NativePopupButton(
  val label: String,
  val callback: (() -> Unit)? = null
)

data class AlertButton(
  val label: String,
  val callback: (() -> Unit)? = null,
  val buttonStyle: AlertButtonStyle = AlertButtonStyle.PRIMARY_CENTER
)

enum class ToastType {
  SUCCESS,
  ERROR,
  WARNING
}

enum class AlertButtonStyle {
  PRIMARY_CENTER,
  PRIMARY_BOTTOM,
  BORDERLESS_UNDERLINE_CENTER
}
