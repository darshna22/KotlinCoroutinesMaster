package com.example.kotlincorotines.rx.serviceavailability

interface ServiceAvailabilityProvider {

  fun getServiceStatus(code: String): ServiceStatus?

}
