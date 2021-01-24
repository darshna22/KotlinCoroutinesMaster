package com.example.kotlincorotines.rx.serviceavailability

import org.threeten.bp.Clock
import org.threeten.bp.ZonedDateTime

/**
 * [ServiceAvailability] is a static class for checking the availability of
 * services.
 *
 * It must first be initialized with a ServiceAvailabilityProvider
 */
object ServiceAvailability {

  const val SERVICE_UNAVAILABLE_ERROR_CODE = "80999"

  private lateinit var provider: ServiceAvailabilityProvider
  private var clock = Clock.systemDefaultZone()

  /**
   * Returns `true` if the service [code] is available.
   *
   * If no service availability data is found for [code], defaults to returning
   * [defaultValue].
   */
  fun isAvailable(code: String, defaultValue: Boolean = true): Boolean {
    val status = provider.getServiceStatus(code)

    if (status?.maintenancePeriod == null) {
      return defaultValue
    }

    if (status.isWithinMaintenancePeriod(ZonedDateTime.now(clock))) {
      return false
    }

    return true
  }

  /**
   * Returns the service status for [code], or `null` if not available.
   */
  fun getStatus(code: String): ServiceStatus? {
    return provider.getServiceStatus(code)
  }

  /**
   * Sets the data [provider].
   */
  fun setProvider(provider: ServiceAvailabilityProvider) {
    ServiceAvailability.provider = provider
  }

  /**
   * Sets the [clock] used to perform time-related operations.
   */
  fun setClock(clock: Clock) {
    ServiceAvailability.clock = clock
  }

}
