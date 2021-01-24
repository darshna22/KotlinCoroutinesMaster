package com.example.kotlincorotines.rx

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Adds this disposable to the `disposableBag`.
 */
fun Disposable.disposedBy(disposableBag: CompositeDisposable) {
  disposableBag.add(this)
}

/**
 * Adds this disposable to the view model's [CompositeDisposable].
 */
fun Disposable.disposedBy(vm: BaseViewModel) {
  vm.addDisposableInternal(this)
}
