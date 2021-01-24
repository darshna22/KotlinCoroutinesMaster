package com.example.kotlincorotines.rx

import org.threeten.bp.ZonedDateTime
import th.co.ktb.next.libs.i18n.T
import th.co.ktb.next.libs.i18n.models.SupportedLanguage

data class ServiceStatus(
  val errorTitleEn: String,
  val errorTitleTh: String,
  val errorMessageEn: String,
  val errorMessageTh: String,
  val maintenancePeriod: Pair<ZonedDateTime, ZonedDateTime>?
) {

  /**
   * Returns `true` if [dateTime] is within the [maintenancePeriod].
   */
  fun isWithinMaintenancePeriod(dateTime: ZonedDateTime): Boolean {
    val maintenanceStartDate = maintenancePeriod?.first
    val maintenanceEndDate = maintenancePeriod?.second

    if (maintenanceStartDate == null || maintenanceEndDate == null) {
      return false
    }

    return dateTime.isAfter(maintenanceStartDate) && dateTime.isBefore(maintenanceEndDate)
  }

  /**
   * Returns the error title for [language]. Defaults to the user's current
   * language.
   */
  fun getErrorTitle(language: SupportedLanguage = T.getLanguage()): String {
    return when (language) {
      SupportedLanguage.ENGLISH -> errorTitleEn
      SupportedLanguage.THAI -> errorTitleTh
      else -> errorTitleEn
    }
  }

  /**
   * Returns the error title for [language]. Defaults to the user's current
   * language.
   */
  fun getErrorMessage(language: SupportedLanguage = T.getLanguage()): String {
    return when (language) {
      SupportedLanguage.ENGLISH -> errorMessageEn
      SupportedLanguage.THAI -> errorMessageTh
      else -> errorMessageEn
    }
  }

}
