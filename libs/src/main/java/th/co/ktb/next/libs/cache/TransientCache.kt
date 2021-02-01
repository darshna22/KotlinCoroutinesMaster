package th.co.ktb.next.libs.cache

import org.threeten.bp.Clock

/**
 * [TransientCache] is a class that provides the ability to cache an object [T]
 * in-memory.
 */
open class TransientCache<T> constructor(
  private val clock: Clock,
  private val maxAge: Long? = null
) {

  private var data: T? = null
  private var cachedAt: Long? = null

  /**
   * Stores [value] into the cache.
   */
  fun set(value: T) {
    this.data = value
    this.cachedAt = clock.instant().epochSecond
  }

  /**
   * Returns the cached value, or `null` if unavailable.
   *
   * You may optionally provide a [maxAge] value (in seconds) to check if the
   * cached value is still fresh. Returns `null` if the cached value is older
   * than [maxAge].
   */
  fun get(): T? {
    if (maxAge != null && isCacheExpired(maxAge)) {
      return null
    }
    return data
  }

  /**
   * Returns `true` if the cache contains a value.
   *
   * You may optionally provide a [maxAge] value (in seconds) to check if the
   * cached value is still fresh. Returns `false` if the cached value is older
   * than [maxAge].
   */
  fun has(): Boolean {
    if (maxAge != null && isCacheExpired(maxAge)) {
      return false
    }
    return data != null
  }

  /**
   * Clears the cache.
   */
  fun clear() {
    this.data = null
    this.cachedAt = null
  }

  private fun isCacheExpired(maxAge: Long): Boolean {
    cachedAt?.let {
      return clock.instant().epochSecond - it > maxAge
    }
    return false
  }

}
