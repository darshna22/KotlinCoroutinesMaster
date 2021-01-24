package com.example.kotlincorotines.rx

import io.reactivex.disposables.CompositeDisposable

interface Disposer {
  val disposeBag: CompositeDisposable
}
