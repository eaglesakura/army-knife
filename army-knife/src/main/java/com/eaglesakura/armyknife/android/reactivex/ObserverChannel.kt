package com.eaglesakura.armyknife.android.reactivex

import com.eaglesakura.armyknife.runtime.coroutines.DelegateChannel
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

internal class ObserverChannel<T>(private val dispatcher: CoroutineDispatcher = Dispatchers.Main) :
    DelegateChannel<T>(Channel<T>(Channel.UNLIMITED)), Observer<T> {
    private var disposable: Disposable? = null

    private val lock = ReentrantLock()

    private fun dispose(): Unit = lock.withLock {
        Channel<Unit>()
        disposable?.dispose()
        disposable = null
    }

    override fun cancel() {
        dispose()
        super.cancel()
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