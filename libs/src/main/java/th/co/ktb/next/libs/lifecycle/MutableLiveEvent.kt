package th.co.ktb.next.libs.lifecycle

/**
 * [LiveEvent] that exposes an [emit] method.
 */
class MutableLiveEvent<T> : LiveEvent<T>() {
  /**
   * Emits a new event with [value]. If there are active observers, the value
   * will be dispatched to them.
   */
  fun emit(value: T) {
    pending.set(true)
    super.postValue(value)
  }

  /**
   * Emits a new event with no value (`null`). If there are active observers,
   * the value will be dispatched to them.
   */
  fun call() {
    pending.set(true)
    super.postValue(null)
  }
}
