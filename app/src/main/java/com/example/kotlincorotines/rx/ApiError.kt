package com.example.kotlincorotines.rx

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.SerializedName
import th.co.ktb.next.libs.i18n.T

/**
 * Represents an error response from the API.
 */
data class ApiError(
  /**
   * The error response code.
   */
  @SerializedName("code") val code: String? = null,

  /**
   * User-friendly title that should be shown to the user.
   */
  @SerializedName("title") val title: String? = null,

  /**
   * User-friendly message that should be shown to the user.
   */
  @SerializedName("message") val message: String? = null,

  /**
   * Technical error message used for troubleshooting.
   */
  @SerializedName("description") val description: String? = null,

  /**
   * Additional information related to this error.
   */
  @SerializedName("additionalInfo") val additionalInfo: AdditionalInfo? = null
) {

  /**
   * Returns a display-friendly version of the additional error information.
   */
  fun getAdditionalErrorInformation(): String? {
    if (code == null) {
      return null
    }

    return buildString {
      append("${T.get("common_error_code_label")}: $code")
      additionalInfo?.entityInfo?.let { append(" - $it") }
    }
  }

  data class AdditionalInfo(
    @SerializedName("entityInfo") val entityInfo: String,
    @SerializedName("errorValueMap") val errorValueMap: HashMap<String, String>
  )

  companion object {

    fun fromJsonString(body: String?, gson: Gson): ApiError? {
      if (body == null) return null
      return try {
        gson.fromJson(body, ApiError::class.java)
      } catch (e: JsonSyntaxException) {
        null
      }
    }

  }

}
