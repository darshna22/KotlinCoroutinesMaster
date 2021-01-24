package com.example.kotlincorotines.rx

interface AppTamperingHelper {

  /**
   * Start the app tampering protection.
   */
  fun start()

  /**
   * Check the app tampering status of the device.
   *
   * WARNING: This should be called on the main thread to assure synchronicity.
   * Otherwise the app may perform a sensitive action before the fingerprint
   * verification is complete.
   */
  fun check()

}
