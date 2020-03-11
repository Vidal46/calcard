package br.com.calcard.android.app.utils

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

open class ObserverUtils<T> : Observer<T> {
    var genericVariable: T? = null
    override fun onSubscribe(d: Disposable) {}
    override fun onNext(t: T) {
        genericVariable = t
    }

    override fun onError(e: Throwable) {}
    override fun onComplete() {}
}