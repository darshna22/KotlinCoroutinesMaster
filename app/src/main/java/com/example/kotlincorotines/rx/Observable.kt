package com.example.kotlincorotines.rx

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import com.example.kotlincorotines.rx.serviceavailability.ServiceAvailability
import com.example.kotlincorotines.rx.serviceavailability.ServiceUnavailableEvent
import th.co.ktb.next.libs.cache.TransientCache
import timber.log.Timber
import java.net.SocketException
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Toggles the loading state of [vm] when the observable is subscribed to and
 * after termination.
 */
fun <T> Observable<T>.doToggleLoadingStateOf(
  vm: BaseViewModel,
  vararg labels: String
): Observable<T> = this
    .doOnSubscribe { vm.setLoadingInternal(true, *labels) }
    .doOnTerminate { vm.setLoadingInternal(false) }

/**
 * Subscribes to this observable with default handling by [vm].
 */
fun <T> Observable<T>.subscribeWithViewModel(
  vm: BaseViewModel,
  onNext: (T) -> Unit
): Disposable = this
    .retryWhenInsufficientScope(vm)
    .retryWhenNoInternetConnectivity(vm)
    .subscribe(onNext, {
      // Log this error for debugging purposes
      Timber.d(it)

      // Check if this exception is a result of a token refresh failure.
      if (it is TokenRefreshException) {
        vm.emitTokenRefreshFailureEvent(it)
        return@subscribe
      }

      // Check if this exception is a result of the service being unavailable.
      if (it is ApiException && it.error?.code == ServiceAvailability.SERVICE_UNAVAILABLE_ERROR_CODE) {
        vm.emitServiceUnavailableEvent(ServiceUnavailableEvent.FromApiResponse(it.error))
        return@subscribe
      }

      if (it is ApiException && it.error?.code == ApiException.ERROR_CODE_APP_VERSION_OUTDATED) {
        vm.emitAppOutdatedEvent(it.error)
        return@subscribe
      }

      // Otherwise delegate to the default error handler.
      vm.handleErrorInternal(it)
    })

/**
 * Subscribes to this observable with default handling by [vm].
 */
fun <T> Observable<T>.subscribeWithViewModel(
  vm: BaseViewModel,
  onNext: (T) -> Unit,
  onError: (Throwable) -> Unit
): Disposable = this
    .retryWhenInsufficientScope(vm)
    .retryWhenNoInternetConnectivity(vm)
    .subscribe(onNext, {
      // Log this error for debugging purposes
      Timber.d(it)

      // Check if this exception is a result of a token refresh failure.
      if (it is TokenRefreshException) {
        vm.emitTokenRefreshFailureEvent(it)
        return@subscribe
      }

      // Check if this exception is a result of the service being unavailable.
      if (it is ApiException && it.error?.code == ServiceAvailability.SERVICE_UNAVAILABLE_ERROR_CODE) {
        vm.emitServiceUnavailableEvent(ServiceUnavailableEvent.FromApiResponse(it.error))
        NextEvent.Builder()
            .forError(it.title.toString(), ErrorDisplayType.FULL_SCREEN)
            .setErrorCodesFrom(it as? ApiException)
            .send()
        return@subscribe
      }

      if (it is ApiException && it.error?.code == ApiException.ERROR_CODE_APP_VERSION_OUTDATED) {
        vm.emitAppOutdatedEvent(it.error)
        NextEvent.Builder()
            .forError(it.title.toString(), ErrorDisplayType.NATIVE_ALERT)
            .setErrorCodesFrom(it as? ApiException)
            .send()
        return@subscribe
      }

      // Otherwise delegate to the default error handler.
      onError.invoke(it)
    })

/**
 * Binds the lifecycle of this [Observable] to a suspended coroutine's [Continuation].
 */
fun <T, U> Observable<T>.bindToContinuation(
  continuation: Continuation<U>,
  resultMapper: (T) -> U
): Observable<T> {
  return this
      .doOnError { continuation.resumeWithException(it) }
      .doAfterNext { continuation.resume(resultMapper.invoke(it)) }
}

/**
 * Adds caching behaviour to this Observable using [cache].
 */
fun <T> Observable<T>.withCache(
  cache: TransientCache<T>,
  ignoreCache: Boolean = false
): Observable<T> {
  if (cache.get() != null && !ignoreCache) {
    return Observable.just(cache.get())
  }
  return this.doOnNext { cache.set(it) }
}

/**
 * Adds a retry behaviour to this observable when encountering an [InsufficientScopeException].
 *
 * The observable will attempt to escalate the user's privileges using [vm] and
 * then retry the observable.
 */
fun <T> Observable<T>.retryWhenInsufficientScope(vm: BaseViewModel): Observable<T> {
  return this.retryWhen { errorStream ->
    errorStream
        .zipWith(Observable.range(1, 5), BiFunction { t: Throwable, i: Int -> Pair(t, i) })
        .flatMap<Any> { pair ->
          val t = pair.first
          val attempt = pair.second

          // Only retry once
          if (attempt > 1) {
            return@flatMap Observable.error(t)
          }

          if (t !is InsufficientScopeException) {
            return@flatMap Observable.error(t)
          }

          return@flatMap Observable.create { emitter ->
            vm.requireScopeInternal(
                listOf(t.requiredScope),
                onError = { emitter.onError(it) },
                onSuccess = { emitter.onNext(t.requiredScope) }
            )
          }
        }
  }
}

/**
 * Adds a retry behaviour to this observable when encountering a [NoConnectivityException].
 *
 * The observable will first display a dialog to the user, before proceeding to
 * retry after the user dismisses the dialog.
 *
 * @see NoConnectivityInterceptor
 */
fun <T> Observable<T>.retryWhenNoInternetConnectivity(vm: BaseViewModel): Observable<T> {
  return this.retryWhen { errorStream ->
    errorStream.flatMap<Any> { t ->
      if (t !is NoConnectivityException && t !is SocketException) {
        return@flatMap Observable.error(t)
      }

      return@flatMap Observable.create { emitter ->
        vm.showNoConnectivityAlert(onConnected = { emitter.onNext("") })
      }
    }
  }
}

fun <T> Observable<T>.onErrorDoNothing(): Observable<Unit> {
  return this.map { Unit }
      .onErrorReturn { Unit }
}
