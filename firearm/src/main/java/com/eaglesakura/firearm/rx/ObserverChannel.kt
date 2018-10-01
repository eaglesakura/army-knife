package com.eaglesakura.firearm.rx

import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.android.Main
import kotlinx.coroutines.experimental.channels.RendezvousChannel
import kotlinx.coroutines.experimental.launch
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

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