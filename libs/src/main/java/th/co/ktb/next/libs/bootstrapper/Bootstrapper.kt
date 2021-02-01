package th.co.ktb.next.libs.bootstrapper

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

/**
 * [Bootstrapper] manages the execution of [Initializer] instances.
 *
 * [Initializer] are classes that represents a singular unit of initialization
 * module to be executed. [Bootstrapper] allows you to manage the order and
 * execution flow of these [Initializer] instances idiomatically.
 *
 * To start the initializing process, call the [initialize] method, which exposes
 * a [Single] to signify its completion.
 */
class Bootstrapper {

  /**
   * Contains multiple groups of [Initializer] instances.
   *
   * Each group of initializers will be executed sequentially, while all initializers
   * in each group will be executed concurrently. Consider the following example:
   *
   * ```
   * initializers = [[A], [B, C, D], [E]]
   * ```
   *
   * 1. `[A]` will execute first; then
   * 2. `[B, C, D]` will all execute concurrently; then
   * 3. `[E]` will be executed last.
   *
   */
  @Suppress("KDocUnresolvedReference")
  private val initializers: MutableList<List<Initializer>> = ArrayList()

  /**
   * Executes the initializers and accumulates the results in a [BootstrapperResult]
   * object.
   */
  fun initialize(): Single<BootstrapperResult> {
    val accumulator = BootstrapperResult()
    return Completable
      .concat(this.initializers.map { this.mergeGroup(it, accumulator) })
      .toSingle { accumulator }
  }

  /**
   * Merges a list of [Initializer] into a single concurrent [Completable].
   */
  private fun mergeGroup(
    initializers: List<Initializer>,
    accumulator: BootstrapperResult
  ): Completable {
    val completables = initializers.map { it.execute(accumulator) }
    return Completable.merge(completables)
  }

  /**
   * Add a new group of [Initializer] to the execution queue.
   *
   * All initializers in this group will be executed concurrently. Multiple
   * initializer groups (ie. multiple calls to [add]) will be executed
   * sequentially in the order of which they were added.
   */
  fun add(vararg initializer: Initializer) {
    this.initializers.add(initializer.toList())
  }

}
