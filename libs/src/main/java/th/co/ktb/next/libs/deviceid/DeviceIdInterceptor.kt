package th.co.ktb.next.libs.deviceid

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * [DeviceIdInterceptor] adds an unique device id to the `X-Device-ID` header
 * to all outgoing requests.
 */
class DeviceIdInterceptor @Inject constructor(

  /** A store to retrieve the device id. */
  private val deviceIdStore: DeviceIdStore

) : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
    val deviceId = deviceIdStore.getDeviceId() ?: deviceIdStore.generate().also { deviceIdStore.setDeviceId(it) }
    val newRequest = request.newBuilder()
      .header("X-Device-ID", deviceId)
      .build()
    return chain.proceed(newRequest)
  }

}
