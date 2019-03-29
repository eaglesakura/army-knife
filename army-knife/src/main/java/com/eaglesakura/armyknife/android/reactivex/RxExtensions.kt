package com.eaglesakura.armyknife.android.reactivex

import androidx.annotation.CheckResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.OnLifecycleEvent
import com.eaglesakura.armyknife.android.extensions.subscribeWithCancel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel

/**
 * Make LiveData from Observable in RxJava.
 *
 * LiveData calls "dispose()" method at Inactive event.
 * You should not call Disposable.dispose() method.
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
fun <T> Observable<T>.toLiveData(): LiveData<T> {
    return RxLiveData(this)
}

/**
 * Make Channel from Observable in RxJava.
 *
 * CAUTION!! You will finished to using, then should call "Channel.close()" method.
 * Or, You may use Channel.consume{} block.
 *
 * Channel calls "dispose()" method at Channel.close() or Channel.cancel().
 * You should not call Disposable.dispose() method.
 *
 * e.g.)
 *
 * observable.toChannel().consume {
 *      // something...
 * }    // Channel.close() on exit.
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
@CheckResult
fun <T> Observable<T>.toChannel(dispatcher: CoroutineDispatcher): Channel<T> {
    return ObserverChannel<T>(dispatcher).also {
        subscribe(it)
    }
}

/**
 * A Disposable interface link to Lifecycle.
 * When lifecycle to destroyed, then call Disposable.dispose() function.
 *
 * If Call "Disposable.dispose()" function before than destroyed.
 * it is supported, you can it.
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
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
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
fun <T> Observable<T>.subscribe(
    lifecycle: Lifecycle,
    onNext: ((next: T) -> Unit)?,
    onError: ((err: Throwable) -> Unit)?,
    onComplete: (() -> Unit)?
): Disposable {
    return subscribe(
            { next -> onNext?.invoke(next) },
            { err -> onError?.invoke(err) },
            { onComplete?.invoke() }
    ).with(lifecycle)
}

/**
 * Lifecycle to RxJava observable.
 *
 * Observable will call "onComplete()" function at lifecycle in "Event.ON_DESTROY".
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
fun Lifecycle.toObservable(): Observable<Lifecycle.Event> {
    val result = PublishSubject.create<Lifecycle.Event>()

    addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
        fun onAny(@Suppress("UNUSED_PARAMETER") source: LifecycleOwner, event: Lifecycle.Event) {
            result.onNext(event)
            if (event == Lifecycle.Event.ON_DESTROY) {
                result.onComplete()
            }
        }
    })
    return result
}