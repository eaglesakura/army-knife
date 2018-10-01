package com.eaglesakura.armyknife.rx

import androidx.annotation.CheckResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import com.eaglesakura.armyknife.android.extensions.subscribeWithCancel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.channels.Channel

/**
 * Make LiveData from Observable in RxJava.
 *
 * LiveData calls "dispose()" method at Inactive event.
 * You should not call Disposable.dispose() method.
 */
fun <T> Observable<T>.toLiveData(): LiveData<T> {
    return RxLiveData(this)
}

/**
 * Make Channel from Observable in RxJava.
 *
 * Channel calls "dispose()" method at Channel.close() or Channel.cancel().
 * You should not call Disposable.dispose() method.
 */
@CheckResult
fun <T> Observable<T>.toChannel(dispatcher: CoroutineDispatcher): Channel<T> {
    return ObserverChannel<T>(dispatcher).also {
        subscribe(it)
    }
}

fun Disposable.with(lifecycle: Lifecycle): Disposable {
    var origin: Disposable? = this

//    PublishSubject.create<Int>()
//            .buffer(2, 1)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe {
//            }.with(lifecycle)

    lifecycle.subscribeWithCancel { event, cancel ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            origin?.dispose()
            origin = null

            cancel()
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
    ).with(lifecycle)
}