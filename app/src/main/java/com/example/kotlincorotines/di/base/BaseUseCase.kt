package com.example.kotlincorotines.di.base

import android.os.Build
import androidx.core.util.Supplier
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class BaseUseCase {
  /**
   * Returns the default background scheduler.
   *
   * By default, it obtains the default scheduler from [defaultBackgroundSchedulerSupplier].
   * Override this method if you want your use case to use a different default scheduler.
   *
   * @return a [Scheduler] to run the observable
   */
  protected fun defaultBackgroundScheduler(): Scheduler = defaultBackgroundSchedulerSupplier.get()

  /**
   * Returns the default observer scheduler.
   *
   * By default, it obtains the default scheduler from [defaultObserverSchedulerSupplier].
   * Override this method if you want your use case to use a different default scheduler.
   *
   * @return a [Scheduler] to run the observable
   */
  protected fun defaultObserverScheduler(): Scheduler = defaultObserverSchedulerSupplier.get()

  companion object {
    // We use a supplier so that it's easily overridable when creating unit tests.
    var defaultBackgroundSchedulerSupplier = Supplier { Schedulers.io() }
    var defaultObserverSchedulerSupplier = Supplier { AndroidSchedulers.mainThread() }
  }

  abstract class WithParams<Params, Result> : BaseUseCase() {
    /**
     * Implement this method to build your use case observable.
     *
     * @params params The parameters to build your use case
     * @return The observable to build.
     */
    protected abstract fun onExecute(params: Params): Observable<Result>

    /**
     * Builds the use case using default schedulers.
     *
     * @param param The parameters to build the use case
     */
    fun build(param: Params): Observable<Result> {
      return this.build(param, this.defaultBackgroundScheduler(), this.defaultObserverScheduler())
    }

    /**
     * Builds the use case using the given schedulers.
     *
     * @param param The parameters to build the use case
     * @param backgroundScheduler The scheduler to run the observable
     * @param observerScheduler The scheduler to run the observer
     */
    fun build(
      param: Params, backgroundScheduler: Scheduler, observerScheduler: Scheduler
    ): Observable<Result> {
      return Observable.defer {
        return@defer this.onExecute(param)
            .subscribeOn(backgroundScheduler)
            .observeOn(observerScheduler)
      }
    }

    /**
     * Builds the use case without configuring any schedulers.
     *
     * @param param The parameters to build the use case
     */
    fun buildWithoutSchedulers(param: Params): Observable<Result> {
      return Observable.defer { this.onExecute(param) }
    }
  }

  abstract class WithoutParams<Result> : BaseUseCase() {
    /**
     * Implement this method to build your use case observable.
     *
     * @return The observable to build.
     */
    protected abstract fun onExecute(): Observable<Result>

    /**
     * Builds the use case using the default schedulers.
     */
    fun build(): Observable<Result> {
      return this.build(this.defaultBackgroundScheduler(), this.defaultObserverScheduler())
    }

    /**
     * Builds the use using the given schedulers.
     *
     * @param backgroundScheduler The scheduler to run the observable
     * @param observerScheduler The scheduler to run the observer
     */
    fun build(backgroundScheduler: Scheduler, observerScheduler: Scheduler): Observable<Result> {
      return Observable.defer {
        return@defer this.onExecute()
            .subscribeOn(backgroundScheduler)
            .observeOn(observerScheduler)
      }
    }

    /**
     * Builds the use case without configuring any schedulers.
     */
    fun buildWithoutSchedulers(): Observable<Result> {
      return Observable.defer { this.onExecute() }
    }
  }

}
