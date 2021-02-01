package th.co.ktb.next.libs.bootstrapper

import io.reactivex.rxjava3.core.Completable

/**
 * An [Initializer] represents a unit of initialisation logic to be executed.
 */
abstract class Initializer {

  /**
   * This function can optionally store the result of this initialization logic
   * in the [accumulator] object.
   */
  abstract fun execute(accumulator: BootstrapperResult): Completable

  /**
   * Halts the application bootstrap process.
   */
  fun halt(initializer: Initializer, showErrorScreen: Boolean = true) {
    throw BootstrapperException(
      initializer = initializer::class.java,
      showDefaultErrorScreen = showErrorScreen
    )
  }

}
