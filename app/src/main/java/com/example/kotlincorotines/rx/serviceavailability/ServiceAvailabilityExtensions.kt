package com.example.kotlincorotines.rx.serviceavailability

import com.example.kotlincorotines.rx.BaseActivity
import com.example.kotlincorotines.rx.BaseFragment
import com.example.kotlincorotines.rx.BaseViewModel


/**
 * Checks the service availability of [code] before executing [onAvailable].
 */
fun BaseViewModel.checkService(
  code: String,
  onUnavailable: (() -> Unit)? = null,
  onAvailable: () -> Unit
) {
  if (ServiceAvailability.isAvailable(code)) {
    onAvailable.invoke()
    return
  }

  if (onUnavailable != null) {
    onUnavailable.invoke()
    return
  }

  ServiceAvailability.getStatus(code)?.let {
    val event = ServiceUnavailableEvent.FromServiceAvailability(code, it)
    emitServiceUnavailableEvent(event)
  }
}

/**
 * Checks the service availability of [code] before executing [onAvailable].
 */
fun BaseFragment.checkService(
  code: String,
  onUnavailable: (() -> Unit)? = null,
  onAvailable: () -> Unit
) {
  if (ServiceAvailability.isAvailable(code)) {
    onAvailable.invoke()
    return
  }

  if (onUnavailable != null) {
    onUnavailable.invoke()
    return
  }

  ServiceAvailability.getStatus(code)?.let {
    val event = ServiceUnavailableEvent.FromServiceAvailability(code, it)
    (activity as? BaseActivity)?.onServiceUnavailable(event)
  }
}
