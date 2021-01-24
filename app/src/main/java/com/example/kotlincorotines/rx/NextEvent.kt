package com.example.kotlincorotines.rx

import android.os.Parcelable
import com.zhuinden.statebundle.StateBundle
import kotlinx.android.parcel.Parcelize
import com.example.kotlincorotines.rx.analytics.Analytics
import timber.log.Timber
import java.math.BigDecimal

@Parcelize
data class NextEvent(
  val eventType: Type = Type.ACTION,
  val eventAction: String? = null,
  val eventCategory: String? = null,
  val eventLabel: String? = null,
  val eventValue: Long? = null,
  val itemOrder: Int? = null,
  val paymentDestination: String? = null,
  val information: String? = null,
  val httpStatusCode: String? = null,
  val errorCode: String? = null,
  val isFinancialTransactionSystemError: Boolean? = null,
  val errorTitle: String? = null,
  val errorDisplayType: ErrorDisplayType? = null,
  val authenticationMethod: AuthMethod? = null,
  val searchTerm: String? = null,
  val billerName: String? = null,
  val productCategory: String? = null,
  val transactionFee: BigDecimal? = null,
  val scheduleType: ScheduleType? = null,
  val duration: Int? = null,
  val itemId: String? = null,
  val migrationLabel: String? = null,
  val fundCode: String? = null,
  val fundType: String? = null
) : Parcelable {

  /**
   *  Send this event to [Analytics].
   */
  fun send() {
    try {
      val params = this@NextEvent.asBundle()

      if (eventAction == null) {
        return
      }

      when (eventType) {
        Type.ACTION -> Analytics.logEvent(eventAction, params)
        Type.VIEW -> Analytics.logView(eventAction, params)
      }
    } catch (t: Throwable) {
      Timber.tag("ANALYTICS").e(t)
    }
  }

  /**
   * Converts a [NextEvent] into a [StateBundle].
   */
  private fun asBundle(): StateBundle {
    return StateBundle().apply {
      putString(EVENT_ACTION, eventAction)
      putSerializable(EVENT_TYPE, eventType)

      eventCategory?.let { putString(EVENT_CATEGORY, it) }
      eventLabel?.let { putString(EVENT_LABEL, it) }
      eventValue?.let { putLong(EVENT_VALUE, it) }
      paymentDestination?.let { putString(PAYMENT_DESTINATION, it) }
      information?.let { putString(INFORMATION, it) }
      httpStatusCode?.let { putString(HTTP_STATUS_CODE, it) }
      errorCode?.let { putString(ERROR_CODE, it) }
      isFinancialTransactionSystemError?.let {
        putBoolean(IS_FINANCIAL_TRANSACTION_SYSTEM_ERROR, it)
      }
      itemOrder?.let { putInt(PARAM_ITEM_ORDER, it) }
      errorTitle?.let { putString(ERROR_TITLE, it) }
      errorDisplayType?.let { putSerializable(ERROR_DISPLAY_TYPE, it) }
      authenticationMethod?.let { putSerializable(AUTHENTICATION_TRIGGERED, it) }
      searchTerm?.let { putString(PARAM_SEARCH_TERM, it) }
      billerName?.let { putString(PARAM_BILLER_NAME, it) }
      productCategory?.let { putString(PARAM_PRODUCT_CATEGORY, it) }
      transactionFee?.let { putSerializable(TRANSACTION_FEE, it) }
      scheduleType?.let { putSerializable(SCHEDULE_TYPE, it) }
      duration?.let { putInt(DURATION, it) }
      itemId?.let { putString(PARAM_ITEM_ID, it) }
      migrationLabel?.let { putString(PARAM_MIGRATION_LABEL, it) }
      fundCode?.let { putString(FUND_CODE, it) }
      fundType?.let { putString(FUND_TYPE, it) }
    }
  }

  object PinPurpose {
    const val SESSION = "Session Pin"
    const val TRANSACTION = "Transaction Pin"
  }

  enum class Type {
    VIEW,
    ACTION
  }

  enum class AuthMethod(val value: String) {
    BIOMETRICS("Biometrics"),
    PIN("Pin"),
    NONE("No Auth");

    companion object {
      fun fromNextScope(scope: NextScope?): AuthMethod? {
        if (scope == null) return NONE
        return when (scope) {
          NextScope.PIN -> PIN
          NextScope.BIOMETRIC -> BIOMETRICS
          else -> null
        }
      }
    }
  }

  enum class ScheduleType(val value: String) {
    MONTHLY("Monthly"),
    ONCE("Once")
  }

  companion object {
    const val EVENT_CLICK = "Click"
    const val EVENT_SLIDE = "Slide"
    const val EVENT_ERROR = "Error"
    const val EVENT_COMPLETED = "Completed"
    const val EVENT_AUTHENTICATE = "Authenticate"
    const val EVENT_MIGRATION = "Migration"

    private const val EVENT_TYPE = "EVENT_TYPE"
    private const val EVENT_ACTION = "EVENT_ACTION"
    private const val EVENT_CATEGORY = "EVENT_CATEGORY"
    private const val EVENT_VALUE = "EVENT_VALUE"
    private const val EVENT_LABEL = "EVENT_LABEL"
    private const val PAYMENT_DESTINATION = "PAYMENT_DESTINATION"
    private const val INFORMATION = "INFORMATION"
    private const val HTTP_STATUS_CODE = "HTTP_STATUS_CODE"
    private const val ERROR_CODE = "ERROR_CODE"
    private const val PARAM_ITEM_ORDER = "PARAM_ITEM_ORDER"
    private const val IS_FINANCIAL_TRANSACTION_SYSTEM_ERROR =
      "IS_FINANCIAL_TRANSACTION_SYSTEM_ERROR"
    private const val ERROR_TITLE = "ERROR_TITLE"
    private const val ERROR_DISPLAY_TYPE = "ERROR_DISPLAY_TYPE"
    private const val AUTHENTICATION_TRIGGERED = "AUTHENTICATION_TRIGGERED"
    private const val PARAM_SEARCH_TERM = "PARAM_SEARCH_TERM"
    private const val PARAM_BILLER_NAME = "PARAM_BILLER_NAME"
    private const val PARAM_PRODUCT_CATEGORY = "PARAM_PRODUCT_CATEGORY"
    private const val TRANSACTION_FEE = "TRANSACTION_FEE"
    private const val SCHEDULE_TYPE = "SCHEDULE_TYPE"
    private const val DURATION = "DURATION"
    private const val PARAM_ITEM_ID = "PARAM_ITEM_ID"
    private const val PARAM_MIGRATION_LABEL = "PARAM_MIGRATION_LABEL"
    private const val COMPANY_NAME = "COMPANY_NAME"
    private const val PLAN_NAME = "PLAN_NAME"
    private const val STATUS = "STATUS"
    private const val PRODUCT_TYPE_NAME = "PRODUCT_TYPE_NAME"
    private const val BANNER_TYPE = "BANNER_TYPE"
    private const val FUND_CODE = "FUND_CODE"
    private const val FUND_TYPE = "FUND_TYPE"

    /**
     * Converts a [params] bundle to a [NextEvent].
     */
    fun fromBundle(eventName: String, params: StateBundle?): NextEvent {
      return Builder()
          .setAction(eventName)
          .setType(params?.getSerializable(EVENT_TYPE) as? Type ?: Type.ACTION)
          .setCategory(params?.getString(EVENT_CATEGORY))
          .setValue(
              params?.getLong(EVENT_VALUE)
                  .takeIf { params?.containsKey(EVENT_VALUE) == true })
          .setLabel(params?.getString(EVENT_LABEL))
          .setPaymentDestination(params?.getString(PAYMENT_DESTINATION))
          .setInformation(params?.getString(INFORMATION))
          .setHttpStatusCode(params?.getString(HTTP_STATUS_CODE))
          .setErrorCode(params?.getString(ERROR_CODE))
          .setIsFinancialTransactionSystemError(
              params?.getBoolean(IS_FINANCIAL_TRANSACTION_SYSTEM_ERROR)
                  ?.takeIf { params.containsKey(IS_FINANCIAL_TRANSACTION_SYSTEM_ERROR) }
          )
          .setItemOrder(
              params?.getInt(PARAM_ITEM_ORDER)
                  .takeIf { params?.containsKey(PARAM_ITEM_ORDER) == true }
          )
          .setErrorTitle(params?.getString(ERROR_TITLE))
          .setErrorDisplayType(params?.getSerializable(ERROR_DISPLAY_TYPE) as? ErrorDisplayType)
          .setAuthenticationMethod(params?.getSerializable(AUTHENTICATION_TRIGGERED) as? AuthMethod)
          .setSearchTerm(params?.getString(PARAM_SEARCH_TERM))
          .setBillerName(params?.getString(PARAM_BILLER_NAME))
          .setProductCategory(params?.getString(PARAM_PRODUCT_CATEGORY))
          .setTransactionFee(params?.getSerializable(TRANSACTION_FEE) as? BigDecimal)
          .setScheduleType(params?.getSerializable(SCHEDULE_TYPE) as? ScheduleType)
          .setDuration(params?.getInt(DURATION).takeIf { params?.containsKey(DURATION) == true })
          .setItemId(params?.getString(PARAM_ITEM_ID))
          .setMigrationLabel(params?.getString(PARAM_MIGRATION_LABEL))
          .setFundCode(params?.getString(FUND_CODE))
          .setFundType(params?.getString(FUND_TYPE))
          .build()
    }

  }

  data class Builder(
    private var eventType: Type = Type.ACTION,
    private var eventAction: String? = null,
    private var eventCategory: String? = null,
    private var eventValue: Long? = null,
    private var eventLabel: String? = null,
    private var paymentDestination: String? = null,
    private var information: String? = null,
    private var httpStatusCode: String? = null,
    private var errorCode: String? = null,
    private var itemOrder: Int? = null,
    private var isFinancialTransactionSystemError: Boolean? = null,
    private var errorTitle: String? = null,
    private var errorDisplayType: ErrorDisplayType? = null,
    private var authenticationMethod: AuthMethod? = null,
    private var searchTerm: String? = null,
    private var billerName: String? = null,
    private var productCategory: String? = null,
    private var transactionFee: BigDecimal? = null,
    private var scheduleType: ScheduleType? = null,
    private var duration: Int? = null,
    private var itemId: String? = null,
    private var migrationLabel: String? = null,
    private var fundCode: String? = null,
    private var fundType: String? = null
  ) {

    /**
     * Copy values from an existing [nextEvent].
     */
    fun copyFrom(nextEvent: NextEvent?) = apply {
      if (nextEvent == null) {
        return@apply
      }
      eventType = nextEvent.eventType
      eventAction = nextEvent.eventAction
      eventCategory = nextEvent.eventCategory
      eventValue = nextEvent.eventValue
      eventLabel = nextEvent.eventLabel
      paymentDestination = nextEvent.paymentDestination
      information = nextEvent.information
      httpStatusCode = nextEvent.httpStatusCode
      errorCode = nextEvent.errorCode
      isFinancialTransactionSystemError = nextEvent.isFinancialTransactionSystemError
      errorTitle = nextEvent.errorTitle
      errorDisplayType = nextEvent.errorDisplayType
      authenticationMethod = nextEvent.authenticationMethod
      searchTerm = nextEvent.searchTerm
      billerName = nextEvent.billerName
      productCategory = nextEvent.productCategory
      transactionFee = nextEvent.transactionFee
      scheduleType = nextEvent.scheduleType
      duration = nextEvent.duration
      itemId = nextEvent.itemId
      itemOrder = nextEvent.itemOrder
      migrationLabel = nextEvent.migrationLabel
      fundCode = nextEvent.fundCode
      fundType = nextEvent.fundType
    }

    /**
     * Sets a [NextEvent] for a view event.
     */
    fun forView(viewName: String) = apply {
      this.eventType = Type.VIEW
      this.eventAction = viewName
    }

    /**
     * Sets a [NextEvent] for a click event.
     */
    fun forClick(
      eventLabel: String, eventValue: Long? = null, eventCategory: String? = null
    ) = apply {
      this.eventAction = EVENT_CLICK
      this.eventLabel = eventLabel
      this.eventType = Type.ACTION
      eventCategory?.let { this.eventCategory = it }
      eventValue?.let { this.eventValue = it }
    }

    /**
     * Sets a [NextEvent] for an error event.
     */
    fun forError(
      eventLabel: String,
      displayType: ErrorDisplayType
    ) = apply {
      this.eventAction = EVENT_ERROR
      this.eventLabel = eventLabel
      this.eventType = Type.ACTION
      this.errorDisplayType = displayType
    }

    /**
     * Sets a [NextEvent] for an migration event.
     */
    fun forMigration(
      eventLabel: String,
      migrationLabel: String? = null
    ) = apply {
      this.eventCategory = EVENT_MIGRATION
      this.eventAction = EVENT_MIGRATION
      this.eventLabel = eventLabel
      this.migrationLabel = migrationLabel
    }

    /**
     * Enriches the [errorCode] and [httpStatusCode] from [t].
     *
     * Additionally if [prependErrorCodeToLabel] is `true`, then the error code
     * from [t] will be prepended to the [eventLabel] if set.
     */
    fun setErrorCodesFrom(t: ApiException?, prependErrorCodeToLabel: Boolean = true) = apply {
      this.errorCode = t?.error?.code
      this.httpStatusCode = t?.response?.code?.toString()

      if (prependErrorCodeToLabel) {
        this.eventLabel = listOfNotNull(t?.error?.code, eventLabel)
          .joinToString(": ")
          .takeIfNotBlank()
      }
    }

    /**
     * Sets a [NextEvent] for a slide event.
     */
    fun forSlide(
      eventLabel: String, eventValue: Long? = null, eventCategory: String? = null
    ) = apply {
      this.eventAction = EVENT_SLIDE
      this.eventLabel = eventLabel
      this.eventType = Type.ACTION
      eventCategory?.let { this.eventCategory = it }
      eventValue?.let { this.eventValue = it }
    }

    fun setType(type: Type) = apply {
      this.eventType = type
    }

    fun setAction(action: String) =
      apply { this.eventAction = action }

    fun setCategory(category: String?) =
      apply { this.eventCategory = category }

    fun setValue(value: Long?) =
      apply { this.eventValue = value }

    fun setLabel(label: String?) =
      apply { this.eventLabel = label }

    fun setPaymentDestination(paymentDestination: String?) =
      apply { this.paymentDestination = paymentDestination }

    fun setInformation(information: String?) =
      apply { this.information = information }

    fun setHttpStatusCode(httpStatusCode: String?) =
      apply { this.httpStatusCode = httpStatusCode }

    fun setErrorCode(errorCode: String?) =
      apply { this.errorCode = errorCode }

    fun setItemOrder(order: Int?) = apply { this.itemOrder = order }

    fun setIsFinancialTransactionSystemError(isFinancialTransactionSystemError: Boolean?) =
      apply { this.isFinancialTransactionSystemError = isFinancialTransactionSystemError }

    fun setErrorTitle(errorTitle: String?) =
      apply { this.errorTitle = errorTitle }

    fun setErrorDisplayType(errorDisplayType: ErrorDisplayType?) =
      apply { this.errorDisplayType = errorDisplayType }

    fun setAuthenticationMethod(authenticationMethod: AuthMethod?) =
      apply { this.authenticationMethod = authenticationMethod }

    fun setSearchTerm(searchTerm: String?) = apply {
      this.searchTerm = searchTerm
    }

    fun setBillerName(billerName: String?) = apply {
      this.billerName = billerName
    }

    fun setProductCategory(productCategory: String?) = apply {
      this.productCategory = productCategory
    }

    fun setTransactionFee(transactionFee: BigDecimal?) =
      apply { this.transactionFee = transactionFee }

    fun setScheduleType(scheduleType: ScheduleType?) =
      apply { this.scheduleType = scheduleType }

    fun setDuration(duration: Int?) =
      apply { this.duration = duration }

    fun setItemId(itemId: String?) =
      apply { this.itemId = itemId }

    fun setMigrationLabel(migrationLabel: String?) =
      apply { this.migrationLabel = migrationLabel }

    fun setFundCode(fundCode: String?) =
        apply { this.fundCode = fundCode }

    fun setFundType(fundType: String?) =
        apply { this.fundType = fundType }

    fun build() = NextEvent(
        eventType = eventType,
        eventAction = eventAction,
        eventCategory = eventCategory,
        eventValue = eventValue,
        eventLabel = eventLabel,
        paymentDestination = paymentDestination,
        information = information,
        httpStatusCode = httpStatusCode,
        errorCode = errorCode,
        isFinancialTransactionSystemError = isFinancialTransactionSystemError,
        errorTitle = errorTitle,
        errorDisplayType = errorDisplayType,
        authenticationMethod = authenticationMethod,
        searchTerm = searchTerm,
        billerName = billerName,
        productCategory = productCategory,
        transactionFee = transactionFee,
        scheduleType = scheduleType,
        duration = duration,
        itemId = itemId,
        itemOrder = itemOrder,
        migrationLabel = migrationLabel,
        fundCode = fundCode,
        fundType = fundType
    )

    fun send() {
      build().send()
    }

  }

}
