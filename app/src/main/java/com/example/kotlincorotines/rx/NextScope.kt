package com.example.kotlincorotines.rx

import com.google.gson.annotations.SerializedName

/**
 * Declares the possible authentication scopes required in this app.
 */
enum class NextScope {

  // WARNING: The ordinal positions of these enums *matter*! Scopes are
  // hierarchical, with the lower ones having more "power" then the before.

  @SerializedName("none")
  NONE,

  @SerializedName("oauth")
  OAUTH,

  @SerializedName("resetpin")
  RESET_PIN,

  @SerializedName("tnc")
  TNC,

  @SerializedName("onboarding")
  ONBOARDING,

  @SerializedName("registration")
  REGISTRATION,

  @SerializedName("mnp")
  CUSTOMER_CONSENT,

  @SerializedName("ekyc")
  EKYC,

  @SerializedName("mnp_ekyc")
  CUSTOMER_CONSENT_EKYC,

  @SerializedName("prelogin")
  PRELOGIN,

  @SerializedName("biometric")
  BIOMETRIC,

  @SerializedName("pin")
  PIN;



  companion object {
    fun fromPrimitive(scope: Int): NextScope? = when (scope) {
      Scope.OAUTH -> OAUTH
      Scope.TNC -> TNC
      Scope.REGISTRATION -> REGISTRATION
      Scope.PRELOGIN -> PRELOGIN
      Scope.BIOMETRIC -> BIOMETRIC
      Scope.PIN -> PIN
      Scope.RESET_PIN -> RESET_PIN
      Scope.CUSTOMER_CONSENT -> CUSTOMER_CONSENT
      Scope.EKYC -> EKYC
      Scope.CUSTOMER_CONSENT_EKYC -> CUSTOMER_CONSENT_EKYC
      Scope.ONBOARDING -> ONBOARDING
      else -> null
    }

    /**
     * A user will be denoted as “logged in“ if they have *any* of these scopes.
     */
    val LOGGED_IN_SCOPES = listOf(PIN, BIOMETRIC)
  }
}

/**
 * A primitive representation of [NextScope] so that it can be used in annotations.
 */
object Scope {
  const val OAUTH = 1
  const val TNC = 2
  const val REGISTRATION = 3
  const val PRELOGIN = 4
  const val PIN = 5
  const val BIOMETRIC = 6
  const val RESET_PIN = 7
  const val CUSTOMER_CONSENT = 8
  const val EKYC = 9
  const val CUSTOMER_CONSENT_EKYC = 10
  const val ONBOARDING = 11
}
