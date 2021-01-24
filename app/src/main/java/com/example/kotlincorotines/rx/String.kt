package com.example.kotlincorotines.rx

import android.text.SpannableString
import android.text.style.UnderlineSpan
import th.co.ktb.next.libs.i18n.I18nLayoutInflaterInterceptor
import th.co.ktb.next.libs.i18n.T
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.ParseException

fun String?.takeIfNotBlank(): String? {
  return this.takeIf { it?.isNotBlank() == true }
}

fun String.trimComma(): String {
  return this.replace(",", "")
}

fun String.trimCharacters(vararg chars: String): String {
  return chars.fold(this) { acc, s -> acc.trimCharacter(s) }
}

fun String.trimCharacter(char: String): String {
  return this.replace(char, "")
}

fun String.trimWhiteSpace(): String {
  return this.replace("\\s".toRegex(), "")
}

fun String.trimCommonWhiteSpace(): String {
  val whiteSpaceRegex = "[\n\t\\x0B\r\b\\u000C\\u0007]".toRegex()
  return this.trim().replace(whiteSpaceRegex, "")
}

/**
 * Returns only the digits in this String.
 */
fun String.onlyDigits(): String {
  return this.replace("[^0-9]".toRegex(), "")
}

fun String.toRequiredFieldString(isRequiredField: Boolean): String {
  return if (isRequiredField) this + " *" else this
}

/**
 * Converts this string to an underlined [SpannableString].
 */
fun String.underline(): SpannableString {
  return SpannableString(this).apply {
    setSpan(UnderlineSpan(), 0, this@underline.length, 0)
  }
}

///**
// * Returns `true` if the string contains unusable characters.
// */
//fun String.containsUnusableCharacters(): Boolean {
//  return ValidationUtils.containsUnusableCharacters(this)
//}

fun String?.isZeroOrEmpty(): Boolean {
  return if (this.isNullOrBlank()) {
    true
  } else {
    try {
      DecimalFormat.getInstance().parse(this).toFloat() == BigDecimal.ZERO.toFloat()
    } catch (e: ParseException) {
      true
    }
  }
}

fun String?.isZero(): Boolean {
  return if (this.isNullOrBlank()) {
    false
  } else {
    try {
      DecimalFormat.getInstance().parse(this).toFloat() == BigDecimal.ZERO.toFloat()
    } catch (e: ParseException) {
      true
    }
  }
}

fun String.localizeIfPrefixed(prefix: String = I18nLayoutInflaterInterceptor.PREFIX): String {
  return if (this.startsWith(prefix)) {
    T.get(this.removePrefix(prefix))
  } else {
    this
  }
}
