package th.co.ktb.next.libs.deviceid

import android.content.SharedPreferences
import java.util.*

/**
 * [DeviceIdStore] generates and persists a Device ID token for as long as
 * the application is installed.
 */
class DeviceIdStore constructor(
  private val deviceIdSharedPreferences: SharedPreferences
) {

  fun getDeviceId(): String? {
    return deviceIdSharedPreferences.getString(DATA_KEY, null)
  }

  fun setDeviceId(deviceId: String) {
    deviceIdSharedPreferences.edit()
        .putString(DATA_KEY, deviceId)
        .apply()
  }

  fun getMobileNumber(): String? {
    return deviceIdSharedPreferences.getString(MOBILE_NUMBER_KEY, null)
  }

  fun getNotInThailand(): Boolean? {
    return deviceIdSharedPreferences.getBoolean(NOT_IN_THAILAND, false)
  }

  fun setMobileNumber(mobileNumber: String) {
    deviceIdSharedPreferences.edit()
        .putString(MOBILE_NUMBER_KEY, mobileNumber)
        .apply()
  }

  fun setNotInThailand(notInThailand: Boolean) {
    deviceIdSharedPreferences.edit()
        .putBoolean(NOT_IN_THAILAND, notInThailand)
        .apply()
  }

  fun generate() = "${UUID.randomUUID()}-devc"

  companion object {
    private const val DATA_KEY = "device_id"
    private const val MOBILE_NUMBER_KEY = "mobile_number"
    private const val NOT_IN_THAILAND = "not_in_thailand"
  }

}
