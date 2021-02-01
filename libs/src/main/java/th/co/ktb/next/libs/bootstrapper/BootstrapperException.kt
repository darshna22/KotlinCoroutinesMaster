package th.co.ktb.next.libs.bootstrapper

class BootstrapperException(
  /**
   * The [Initializer] that triggered this exception.
   */
  val initializer: Class<out Initializer>,

  /**
   * Set to `true` if this exception should show the default fatal error screen.
   */
  val showDefaultErrorScreen: Boolean = true
) : RuntimeException()
