package com.example.kotlincorotines.rx

import okhttp3.Response
import th.co.ktb.next.libs.i18n.T

/**
 * [ApiException] is thrown when the API fails to return a successful response.
 *
 * A response is deemed not successful when the HTTP status code is not from any of
 * the 2XX or 3XX series.
 *
 * It may optionally include an [ApiError] object containing the relevant error
 * codes and messages produced by the API. It may also optionally include the
 * [Response] that resulted in this exception.
 */
class ApiException(
  val error: ApiError?,
  val response: Response?
) : RuntimeException() {

  constructor(error: ApiError?) : this(error, null)

  constructor() : this(null, null)

  override val message: String?
    get() = error?.message ?: T.get("common_default_error_message")

  val title: String?
    get() = error?.title ?: T.get("common_default_error_title")

  companion object {
    const val ERROR_CODE_APP_VERSION_OUTDATED = "30020"
    const val ERROR_CODE_SERVICE_TIMEOUT = "80002"
    const val ERROR_CODE_API_TIMEOUT = "FE408"
  }

}
