package com.example.kotlincorotines.rx

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import th.co.ktb.next.libs.i18n.T
import th.co.ktb.next.libs.i18n.models.SupportedLanguage
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import com.example.kotlincorotines.rx.serviceavailability.ServiceUnavailableEvent


abstract class BaseActivity : AppCompatActivity(), LifecycleOwner, CoroutineScope, Disposer {

  protected val job = Job()

  override val coroutineContext = job + Dispatchers.Main

  /**
   * The central handler used to handle privilege escalation requests.
   */
  protected open val privilegeEscalationHandler: PrivilegeEscalationHandler? = null

  protected open val appDistributionUrl: String? = ""

  open val appTamperingHelper: AppTamperingHelper? = null

  private val noConnectivityQueue by lazy { LinkedList<Alert.NoConnectionPopup>() }

  private val isShowingNoConnectivityAlert = AtomicBoolean(false)

  internal val loadingOverlay: LoadingOverlayDialog by lazy {
    LoadingOverlayDialog(context = this, coroutineContext = coroutineContext)
  }

  override val disposeBag by lazy { CompositeDisposable() }

  override fun attachBaseContext(newBase: Context) {
    super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
  }

  override fun onDestroy() {
    super.onDestroy()
    job.cancel()
    disposeBag.dispose()
  }

  /**
   * Set window soft input mode.
   */
  fun setWindowSoftInputMode(mode: Int) {
    window.setSoftInputMode(mode)
  }

  /**
   * Sets the status bar color to [type].
   *
   * The color of [type] is in reference to the *background* of the status bar.
   * ie. [StatusBarType.LIGHT] is meant to be used when the status bar will be
   * against a LIGHT background, and thus the icons will be BLACK.
   */
  fun setStatusBarType(type: StatusBarType) {
    window.decorView.systemUiVisibility = when (type) {
      StatusBarType.DARK -> window.decorView.systemUiVisibility.setLightStatusBarFlag(false)
      StatusBarType.LIGHT -> window.decorView.systemUiVisibility.setLightStatusBarFlag(true)
    }
  }

  private fun Int.setLightStatusBarFlag(isLight: Boolean) = when (isLight) {
    true -> this or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR // add the flag with bitwise OR
    false -> this and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv() // remove the flag with bitwise AND + Inversion
  }

//  /**
//   * Binds default observers to a [viewModel]'s observables.
//   */
//  protected fun subscribeToViewModel(viewModel: BaseViewModel) {
//    viewModel.loadingState.observe(this, Observer {
//      when (it.isLoading) {
//        true -> showLoading(*it.labels.toTypedArray())
//        false -> dismissLoading()
//      }
//    })
//
//    viewModel.onAlertEvent.observe(this, Observer(this::onAlert))
//    viewModel.onPermissionsRequestEvent.observe(this, Observer(this::onPermissionRequest))
//    viewModel.onNavigateBackEvent.observe(this, Observer { onBackPressed() })
//    viewModel.onRequestPrivilegeEscalationEvent.observe(this, Observer {
//      onRequestPrivilegeEscalation(it)
//    })
//    viewModel.onTokenRefreshFailureEvent.observe(this, Observer {
//      (this as? TokenRefreshUiHandler)?.onTokenRefreshFailure(it)
//    })
//    viewModel.onServiceUnavailableEvent.observe(this, Observer {
//      onServiceUnavailable(it)
//    })
//    viewModel.onAppOutdatedEvent.observe(this, Observer {
//      onAppOutdated(it)
//    })
//    viewModel.onLaunchDeepLinkEvent.observe(this, Observer {
//      launchDeeplink(Uri.parse(it.uri), it.internalOnly, it.onNotSupported)
//    })
//    viewModel.onLocalBroadcast.observe(this, Observer {
//      onLocalBroadcast()
//    })
//    viewModel.onCheckAppTampering.observe(this, Observer {
//      appTamperingHelper?.check()
//    })
//    viewModel.onLaunchIntentEvent.observe(this, Observer { intent ->
//      startActivity(intent.first, intent.second)
//    })
//  }

  // LOADING OVERLAY

  /**
   * Displays a fullscreen loading overlay with [labels].
   */
  fun showLoading(vararg labels: String) {
    loadingOverlay.show(labels.toList())
  }

  /**
   * Dismisses the fullscreen loading overlay.
   */
  fun dismissLoading() {
    loadingOverlay.dismiss()
  }

  // ALERT HANDLING

  /**
   * This method is invoked when [vm] emits an `onAlertEvent` event.
   *
   * @see onToastAlert
   * @see onNativePopupAlert
   * @see onCustomPopupAlert
   */
  protected fun onAlert(alert: Alert) {
    when (alert) {
      is Alert.Toast -> showToast(alert)
      is Alert.NativePopup -> showNativePopupAlert(alert)
      is Alert.CustomPopup -> showCustomPopupAlert(alert)
      is Alert.WebBrowser -> showWebBrowserDialog(alert)
      is Alert.NoConnectionPopup -> showNoConnectivityAlert(alert)
    }
  }

  /**
   * Displays a toast alert.
   */
  fun showToast(alert: Alert.Toast) {
    when (alert.type) {
      ToastType.SUCCESS -> ToastUtils.showSuccess(this, alert.message)
      ToastType.WARNING -> ToastUtils.showWarning(this, alert.message)
      ToastType.ERROR -> ToastUtils.showError(this, alert.message)
    }
  }

  /**
   * Displays a native popup alert.
   */
  fun showNativePopupAlert(alert: Alert.NativePopup) {
    val builder = NextDialog.Builder()
      .title(alert.title)
      .message(alert.message)

    builder.positiveButton(alert.positiveButtonLabel, alert.positiveButtonCallback)

    if (alert.negativeButtonLabel != null) {
      builder.negativeButton(alert.negativeButtonLabel, alert.negativeButtonCallback)
    }

    builder.build()
      .show(supportFragmentManager, "nextDialog")
  }

  /**
   * Displays a custom popup alert.
   */
  fun showCustomPopupAlert(alert: Alert.CustomPopup) {
    val dialog = CustomPopupDialog.newInstance(
      title = alert.title,
      message = alert.message,
      heroImage = alert.heroImage,
      onDismiss = alert.onDismiss,
      buttonText = alert.buttonText,
      footer = alert.footer
    )
    dialog.show(supportFragmentManager, "customPopupAlertDialog")
  }

  /**
   * Displays a web browser dialog.
   */
  fun showWebBrowserDialog(alert: Alert.WebBrowser) {
    when (alert.type) {
      WebBrowserType.IN_APP_BROWSER -> {
//        getNavigator().goTo(InternalBrowserScreen(alert.url, alert.title, alert.header))
      }
      WebBrowserType.EXTERNAL_BROWSER -> {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(alert.url))
        try {
          startActivity(intent)
        } catch (e: Exception) {
          // do nothing
        }
      }
    }

  }

  /**
   * Displays a dialog indicating lack of internet connectivity.
   *
   * The function is queued, meaning multiple calls to this function while the
   * first one is still active will be added to a queue instead of displaying
   * concurrent popups.
   */
  fun showNoConnectivityAlert(alert: Alert.NoConnectionPopup) {
    // If there is an existing popup being shown, add it to a queue instead
    if (isShowingNoConnectivityAlert.getAndSet(true)) {
      return noConnectivityQueue.push(alert)
    }

    val dialog = NoConnectionPopupDialog.newInstance(onConnected = {
      isShowingNoConnectivityAlert.set(false)
      alert.onConnected.invoke()

      // Clear the queue
      while (noConnectivityQueue.isNotEmpty()) {
        noConnectivityQueue.poll().onConnected.invoke()
      }
    })

    dialog.show(supportFragmentManager, NoConnectionPopupDialog::class.java.name)
    dialog.isCancelable = false
  }

  // PERMISSION REQUEST

  protected fun onPermissionRequest(request: PermissionRequest) {
    checkPermissions(
      request.permissions,
      request.onDenied,
      request.onRationaleRequired,
      request.onGranted
    )
  }

  // SCOPE ESCALATION

  open fun onRequestPrivilegeEscalation(request: PrivilegeEscalationRequest) {
    privilegeEscalationHandler?.requestPrivilege(request)
  }

  // SERVICE AVAILABILITY

  open fun onServiceUnavailable(event: ServiceUnavailableEvent) {
    val noticeContext = NoticeScreenContext(
      screenTitle = T.get("service_unavailable_page_title"),
      heading = when (event) {
        is ServiceUnavailableEvent.FromServiceAvailability -> event.status.getErrorTitle()
        is ServiceUnavailableEvent.FromApiResponse -> event.error.title ?: ""
      },
      message = when (event) {
        is ServiceUnavailableEvent.FromServiceAvailability -> event.status.getErrorMessage()
        is ServiceUnavailableEvent.FromApiResponse -> event.error.message ?: ""
      },
      buttonLabel = T.get("service_unavailable_ok_button")
    )

//    getNavigator().goTo(NoticeScreen(noticeContext))
  }

  // APP UPDATE
  open fun onAppOutdated(event: ApiError) {
    val alert = Alert.NativePopup(
      title = event.title,
      message = event.message,
      positiveButton = NativePopupButton(
        label = T.get("common_button_ok"),
        callback = {
//          launchDeeplink(Uri.parse(appDistributionUrl))
        }
      )
    )
    showNativePopupAlert(alert)
  }

  open fun onLocalBroadcast() {
    val intent = Intent(DOPA_VERIFICATION_INTENT)
    LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
  }

  fun getDeviceLanguage(): SupportedLanguage {
    val lang = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      resources.configuration.locales[0].isO3Language
    } else {
      resources.configuration.locale.isO3Language
    }

    return SupportedLanguage.fromIso639_2(lang) ?: SupportedLanguage.ENGLISH
  }

  companion object {
    private const val DOPA_VERIFICATION_INTENT = "newnext_local_broadcast_dopa_verification"
  }
}
