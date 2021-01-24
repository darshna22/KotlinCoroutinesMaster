package com.example.kotlincorotines.rx.analytics

import android.app.Application
import com.zhuinden.statebundle.StateBundle
import com.example.kotlincorotines.rx.analytics.Analytics.setProvider

/**
 * [Analytics] is a static class for easily accessible logging of analytics events.
 *
 * This class must be first initialised with an [AnalyticsProvider] to provide it with
 * an analytics service to send events to. This should be done in [Application.onCreate]
 * via the [setProvider] method.
 */
object Analytics {

  private lateinit var provider: AnalyticsProvider

  /**
   * Logs a view event for [viewName].
   */
  fun logView(viewName: String, viewParams: StateBundle? = null) {
    provider.logView(viewName, viewParams)
  }

  /**
   * Log [eventName] with no event parameters.
   */
  fun logEvent(eventName: String) {
    provider.logEvent(eventName, null)
  }

  /**
   * Log [eventName] with [eventParams].
   */
  fun logEvent(eventName: String, eventParams: StateBundle?) {
    provider.logEvent(eventName, eventParams)
  }

  /**
   * Provide an implementation of [AnalyticsProvider] to the static class.
   */
  fun setProvider(provider: AnalyticsProvider) {
    Analytics.provider = provider
  }

}
