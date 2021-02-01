package th.co.ktb.next.libs.validation

import java.util.*

/**
 * [ErrorBag] is an object to contain a list of validation errors.
 */
data class ErrorBag(val errors: LinkedHashMap<String, String>) {

  constructor() : this(LinkedHashMap())

  /**
   * Returns the error for [field], or `null` if there is none.
   */
  fun get(field: String) = this.errors[field]

  /**
   * Returns true if [field] is in error bag.
   */
  fun has(field: String) = this.errors[field] != null

  /**
   * Adds an error. Overrides any existing errors already set.
   */
  fun add(field: String, message: String) {
    this.errors[field] = message
  }

  /**
   * Removes an error.
   */
  fun remove(field: String) {
    this.errors.remove(field)
  }

  /**
   * Returns all errors.
   *
   * @return a [Map] containing all errors
   */
  fun all(): Map<String, String> = this.errors

  /**
   * Gets the first error message.
   *
   * @return The first error message, or `null` if none.
   */
  fun first(): Map.Entry<String, String>? {
    if (this.size() < 1) {
      return null
    }
    return this.errors.entries.iterator().next()
  }

  /**
   * Returns true if the error bag is not empty.
   */
  fun isNotEmpty() = this.errors.isNotEmpty()

  /**
   * Returns the number of errors in the bag.
   */
  fun size() = this.errors.size

  /**
   * Returns an [Iterator] over the entries in the [ErrorBag].
   */
  fun iterator(): Iterator<Map.Entry<String, String>> = this.errors.iterator()

}
