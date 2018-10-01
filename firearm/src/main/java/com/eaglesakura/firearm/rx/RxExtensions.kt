package com.eaglesakura.firearm.rx

import androidx.annotation.CheckResult
import androidx.lifecycle.LiveData
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.android.Main
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.RendezvousChannel
import kotlinx.coroutines.experimental.launch
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock


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

internal class ObserverChannel<T>(private val dispatcher: CoroutineDispatcher = Dispatchers.Main) : RendezvousChannel<T>(), Observer<T> {
    private var disposable: Disposable? = null

    private val lock = ReentrantLock()

    private fun dispose(): Unit = lock.withLock {
        disposable?.dispose()
        disposable = null
    }

    override fun cancel(cause: Throwable?): Boolean {
        dispose()
        return super.cancel(cause)
    }

    override fun close(cause: Throwable?): Boolean {
        dispose()
        return super.close(cause)
    }

    override fun onSubscribe(d: Disposable) {
        disposable = d
    }

    override fun onError(e: Throwable) {
        cancel(e)
    }

    override fun onComplete() {
        close()
    }

    override fun onNext(value: T) {
        GlobalScope.launch(dispatcher) {
            send(value)
        }
    }
}