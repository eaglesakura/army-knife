package com.eaglesakura.armyknife.rx

import androidx.lifecycle.Lifecycle
import com.eaglesakura.armyknife.android.extensions.subscribe
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

fun Disposable.autoDispose(lifecycle: Lifecycle): Disposable {
    var origin: Disposable? = this

//    PublishSubject.create<Int>()
//            .buffer(2, 1)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe {
//
//            }.autoDispose(lifecycle)

    lifecycle.subscribe {
        if (it == Lifecycle.Event.ON_DESTROY) {
            origin?.dispose()
            origin = null
        }
    }

    return object : Disposable {
        override fun isDisposed(): Boolean {
            return origin?.isDisposed ?: true
        }

        override fun dispose() {
            origin?.dispose()
            origin = null
        }
    }
}

/**
 * Subscribe value from any Observable with Lifecycle.
 */
fun <T> Observable<T>.subscribe(lifecycle: Lifecycle,
                                onNext: ((next: T) -> Unit)?,
                                onError: ((err: Throwable) -> Unit)?,
                                onComplete: (() -> Unit)?): Disposable {
    return subscribe(
            { next -> onNext?.invoke(next) },
            { err -> onError?.invoke(err) },
            { onComplete?.invoke() }
    ).autoDispose(lifecycle)
}