package com.example.kotlincorotines.rx

import android.content.Intent
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.core.util.Supplier
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.suspendCancellableCoroutine
import th.co.ktb.next.libs.i18n.T
import th.co.ktb.next.libs.lifecycle.MutableLiveEvent
import timber.log.Timber
import java.io.InterruptedIOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import com.example.kotlincorotines.rx.serviceavailability.ServiceUnavailableEvent

/**
 * Provides a [ViewModel] with several preset observables along with additional
 * helpers and lifecycle methods.
 */
open class BaseViewModel @Inject constructor() : ViewModel(), Disposer {

  override val disposeBag: CompositeDisposable = CompositeDisposable()

  /**
   * A base [NextEvent] that can be used as a template for all events on this
   * ViewModel.
   *
   * @see NextEvent.Builder.copyFrom
   */
  var baseNextEvent: Supplier<NextEvent>? = null

  /**
   * Describes the [LoadingState] of this View Model.
   */
  val loadingState: MutableLiveData<LoadingState> = MutableLiveData()

  /**
   * Emits an event to request for device permissions using a [PermissionRequest].
   */
  val onPermissionsRequestEvent: MutableLiveEvent<PermissionRequest> = MutableLiveEvent()

  /**
   * Emits an event to launch a [DeepLinkEvent].
   */

  /**
   * Emits an event to navigate back. This simulates a device back button press.
   */
  val onNavigateBackEvent: MutableLiveEvent<Unit> = MutableLiveEvent()

  /**
   * Emits an event to send the back analytics event.
   * @warning This does not actually perform any navigation, but only to send
   *          an analytics event.
   */
  val onSendAnalyticsBackEvent: MutableLiveEvent<Unit> = MutableLiveEvent()

  /**
   * Emits an event to request for privilege escalation using a [PrivilegeEscalationRequest].
   */
  val onRequestPrivilegeEscalationEvent = MutableLiveEvent<PrivilegeEscalationRequest>()

  /**
   * Emits an event to notify that a requested service is unavailable.
   */
  val onServiceUnavailableEvent = MutableLiveEvent<ServiceUnavailableEvent>()

  val onAppOutdatedEvent = MutableLiveEvent<ApiError>()

  val onDismissAlertViewEvent: MutableLiveEvent<Unit> = MutableLiveEvent()

  val onAlertEvent: MutableLiveEvent<Alert> = MutableLiveEvent()

  val onTokenRefreshFailureEvent: MutableLiveEvent<TokenRefreshException> = MutableLiveEvent()

  val onLocalBroadcast: MutableLiveEvent<Unit> = MutableLiveEvent()

  val onCheckAppTampering: MutableLiveEvent<Unit> = MutableLiveEvent()

  val onLaunchIntentEvent: MutableLiveEvent<Pair<Intent, Bundle?>> = MutableLiveEvent()

  init {
    loadingState.value = LoadingState(isLoading = false)
  }

  override fun onCleared() {
    super.onCleared()
    this.disposeBag.clear()
  }

  /**
   * Emits a user's intent to navigate back.
   * @see onNavigateBackEvent
   */
  open fun navigateBack() {
    onNavigateBackEvent.call()
  }


  /**
   * Perform a check if the device has been rooted.
   */
  open fun checkAppTampering() {
    onCheckAppTampering.call()
  }

  // SCOPE MANAGEMENT

  /**
   * Returns the highest scope that the current user has.
   */
  protected fun getHighestScope(): NextScope? {
    return tokenServiceSupplier.get().getHighestScope()
  }

  /**
   * Checks if the user has the required [scope] before executing [onSuccess].
   *
   * If the user does not have the required scope, then it will attempt to
   * escalate the scope by emitting a [onRequestPrivilegeEscalationEvent].
   */
  @Suppress("FoldInitializerAndIfToElvis")
  open fun requireScope(
    vararg scopes: NextScope,
    onError: ((Throwable) -> Unit)? = null,
    onSuccess: (scopeRequested: NextScope?) -> Unit
  ) {

    val service = tokenServiceSupplier.get()

    if (service == null) {
      throw IllegalStateException("Attempted to check scope but no TokenService is set.")
    }

    if (scopes.any { service.hasToken(it) }) {
      return onSuccess.invoke(null)
    }

    val request = PrivilegeEscalationRequest(
        scope = scopes.sorted().first(),
        onError = onError ?: { },
        onSuccess = onSuccess
    )

    onRequestPrivilegeEscalationEvent.emit(request)
  }

  open suspend fun requireScope(vararg scopes: NextScope) =
      suspendCancellableCoroutine<NextScope?> { continuation ->
        requireScope(
            *scopes,
            onSuccess = { continuation.resume(it) },
            onError = { continuation.resumeWithException(it) }
        )
      }

  // ERROR HANDLING

  /**
   * Applies default error handling to [t].
   */
  open fun handleError(t: Throwable) {
    when (t) {
      is ApiException -> handleApiException(t)
      is PrivilegeEscalationCancellationException -> {
        // do nothing
      }
      else -> handleGenericException(t)
    }
  }

  protected open fun handleApiException(e: ApiException) {
    showAlert(e)
  }

  protected open fun handleGenericException(e: Throwable) {
    showAlert(e)
  }

  // LOADING EVENTS

  /**
   * Sets the loading state for this view model to `true` with an optional
   * list of [labels] to display.
   *
   * If used with a [DataBindingFragment], this will toggle a full-screen
   * loading overlay.
   */
  open fun showLoading(vararg labels: String) {
    this.loadingState.value = LoadingState(isLoading = true, labels = labels.toList())
  }

  /**
   * Sets the loading state for this view model to `false`.
   */
  open fun dismissLoading() {
    this.loadingState.value = LoadingState(isLoading = false)
  }

  // ALERT DIALOGS

  /**
   * Emits an [alert] event.
   */
  open fun showAlert(alert: Alert) {
    onAlertEvent.emit(alert)
  }

  /**
   * Emits a default alert event for [t] with an optional [onDismiss] callback.
   *
   * @param t The exception that is to be displayed to the user.
   * @param logError If this is `true`, this exception will be logged to the
   *                 central exception logger.
   * @param ignoreExceptions The exception list is to be ignored to do any actions.
   * @param onDismiss The callback to be invoked when the alert is dismissed.
   */
  open fun showAlert(
    t: Throwable,
    logError: Boolean = false,
    ignoreExceptions: List<Class<*>> = listOf(PrivilegeEscalationCancellationException::class.java),
    onDismiss: (() -> Unit)? = null
  ) {
    if (ignoreExceptions.contains(t::class.java)) {
      return
    }

    val alert = Alert.CustomPopup.fromThrowable(t, onDismiss)
    showAlert(alert)

    // Log this error to the central exception logger.
    if (logError) {
      Timber.d(t)
    }

    val isTimeoutError =
      (t as? ApiException)?.response?.code == CODE_TIMEOUT || t is InterruptedIOException || t is SocketTimeoutException

    // Record an analytics event for this error.
    val eventLabel = listOfNotNull(
      if (isTimeoutError) "FE408" else null,
      (t as? ApiException)?.error?.code,
      alert.title,
      alert.message
    ).joinToString(": ")

    NextEvent.Builder()
        .copyFrom(baseNextEvent?.get())
        .setAction(NextEvent.EVENT_ERROR)
        .setLabel(eventLabel)
        .setHttpStatusCode((t as? ApiException)?.response?.code?.toString())
        .setErrorCode((t as? ApiException)?.error?.code)
        .setErrorTitle(alert.title)
        .setErrorDisplayType(ErrorDisplayType.CUSTOM_POPUP)
        .send()
  }

  /**
   * Emits a toast alert event with [message].
   */
  fun showToast(type: ToastType, message: String) {
    onAlertEvent.emit(Alert.Toast(type, message))
  }

  /**
   * Emits a success toast alert event using [message].
   */
  fun showSuccessToast(message: String) {
    onAlertEvent.emit(Alert.Toast(ToastType.SUCCESS, message))
  }

  /**
   * Emits an error toast alert event using [message] and [t].
   */
  fun showErrorToast(message: String? = null, t: Throwable? = null) {
    val toastMessage =
        message ?: (t as? ApiException)?.error?.message ?: T.get("common_default_error_message")
    onAlertEvent.emit(Alert.Toast(ToastType.ERROR, toastMessage))

    NextEvent.Builder()
        .copyFrom(baseNextEvent?.get())
        .forError(toastMessage, ErrorDisplayType.CALLOUT)
        .setErrorCodesFrom(t as? ApiException, prependErrorCodeToLabel = true)
        .send()
  }

  /**
   * Emits a native alert event.
   */
  open fun showNativePopup(
    title: String? = null,
    message: String? = null,
    positiveButton: NativePopupButton? = null,
    negativeButton: NativePopupButton? = null
  ) {
    val alert = Alert.NativePopup(
        title = title,
        message = message,
        positiveButton = positiveButton,
        negativeButton = negativeButton
    )
    onAlertEvent.emit(alert)
  }

  /**
   * Emits a custom alert event.
   */
  open fun showCustomPopup(
    title: String? = null,
    message: String? = null,
    @DrawableRes imageResId: Int? = null,
    buttonText: String? = null,
    onDismiss: (() -> Unit)? = null
  ) {
    val alert = Alert.CustomPopup(
        title = title,
        message = message,
        heroImage = imageResId,
        onDismiss = onDismiss,
        buttonText = buttonText
    )
    onAlertEvent.emit(alert)
  }

  /**
   * Emits a custom alert event.
   */
  open fun showAlertView(
    title: String? = null,
    message: String? = null,
    @DrawableRes imageResId: Int? = null,
    button: AlertButton? = null,
    screenName: String? = null
  ) {
    val alert = Alert.AlertView(
        title = title,
        message = message,
        heroImage = imageResId,
        button = button,
        screenName = screenName
    )
    onAlertEvent.emit(alert)
  }

  /**
   * Emits a web browser event.
   */
  open fun showWebBrowser(
    title: String = "",
    url: String,
    type: WebBrowserType = WebBrowserType.IN_APP_BROWSER,
    header: HashMap<String, String>? = null,
    onDismiss: (() -> Unit)? = null
  ) {
    val alert = Alert.WebBrowser(
        title = title,
        url = url,
        type = type,
        header = header,
        onDismiss = onDismiss
    )
    onAlertEvent.emit(alert)
  }

  /**
   * Emits a no connectivity alert event.
   */
  open fun showNoConnectivityAlert(
    onConnected: () -> Unit
  ) {
    val alert = Alert.NoConnectionPopup(onConnected = onConnected)
    onAlertEvent.emit(alert)
  }

  open fun dismissAlertView() {
    onDismissAlertViewEvent.call()
  }

  protected open fun requirePermissions(
    permissions: List<String>,
    onDenied: ((MultiplePermissionsReport) -> Unit)? = null,
    onRationaleRequired: ((PermissionToken) -> Unit)? = null,
    onGranted: () -> Unit
  ) {
    val alert = PermissionRequest(
        permissions = permissions,
        onDenied = onDenied,
        onRationaleRequired = onRationaleRequired,
        onGranted = onGranted
    )
    onPermissionsRequestEvent.emit(alert)
  }

  protected open fun requirePermission(
    permission: String,
    onDenied: ((MultiplePermissionsReport) -> Unit)? = null,
    onRationaleRequired: ((PermissionToken) -> Unit)? = null,
    onGranted: () -> Unit
  ) {
    requirePermissions(listOf(permission), onDenied, onRationaleRequired, onGranted)
  }

  // INTERNAL FUNCTIONS

  internal fun setLoadingInternal(isLoading: Boolean, vararg labels: String) {
    if (isLoading) showLoading(*labels) else dismissLoading()
  }

  internal fun handleErrorInternal(t: Throwable) {
    this.handleError(t)
  }

  internal fun addDisposableInternal(d: Disposable) {
    this.disposeBag.add(d)
  }

  internal fun requireScopeInternal(
    scopes: List<NextScope>,
    onError: ((Throwable) -> Unit)? = this::handleErrorInternal,
    onSuccess: (scopeRequested: NextScope?) -> Unit
  ) {
    requireScope(
        *scopes.toTypedArray(),
        onError = onError,
        onSuccess = onSuccess
    )
  }

  internal fun emitTokenRefreshFailureEvent(e: TokenRefreshException) {
    onTokenRefreshFailureEvent.emit(e)
  }

  internal fun emitServiceUnavailableEvent(event: ServiceUnavailableEvent) {
    onServiceUnavailableEvent.emit(event)
  }

  internal fun emitAppOutdatedEvent(event: ApiError) {
    onAppOutdatedEvent.emit(event)
  }

  companion object {
    /**
     * Set a global supplier of [TokenService], for use by the ViewModel in any
     * scope escalation requests.
     */
    var tokenServiceSupplier = Supplier<TokenService> {
      throw IllegalStateException("No token service supplier is set.")
    }
    const val CODE_TIMEOUT = 504
  }

  fun broadcastLocalMessage() {
    onLocalBroadcast.call()
  }
}
