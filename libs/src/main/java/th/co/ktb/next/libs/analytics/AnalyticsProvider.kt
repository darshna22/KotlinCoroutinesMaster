package th.co.ktb.next.libs.analytics

import com.zhuinden.statebundle.StateBundle

/**
 * [AnalyticsProvider] provides an unified interface for logging analytics events.
 *
 * This interface abstracts from any specific analytics backend which can be easily changed
 * in the future if needed.
 *
 * To integrate an alternative analytics service, provide an implementation of this
 * interface to the [Analytics] class.
 */
interface AnalyticsProvider {

  /**
   * Logs a screen view.
   */
  fun logView(viewName: String, viewParams: StateBundle? = null)

  /**
   * Logs an analytics event.
   */
  fun logEvent(eventName: String, eventParams: StateBundle? = null)

}
