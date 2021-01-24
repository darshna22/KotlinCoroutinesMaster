package com.example.kotlincorotines.rx.serviceavailability

import com.example.kotlincorotines.rx.ApiError

sealed class ServiceUnavailableEvent {

  /**
   * Represents a [ServiceUnavailableEvent] as a result of checking against
   * the service availability list.
   */
  data class FromServiceAvailability(
    val serviceCode: String,
    val status: ServiceStatus
  ) : ServiceUnavailableEvent()

  /**
   * Represents a [ServiceUnavailableEvent] as a result of an API response.
   */
  data class FromApiResponse(
    val error: ApiError
  ) : ServiceUnavailableEvent()

}
