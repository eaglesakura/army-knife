package com.eaglesakura.firearm.rx

import androidx.annotation.CheckResult
import androidx.lifecycle.*
import io.reactivex.Observable
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.android.Main
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.launch


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


fun <T> newStreamObserver(block: (value: T) -> Unit): Observer<T> {
    return Observer {
        val event = it ?: return@Observer
        block(event)
    }
}

fun <T> newStreamObserverWithForeground(owner: LifecycleOwner, block: (value: T) -> Unit): Observer<T> {
    return newStreamObserver {
        owner.lifecycle.runOnForeground {
            block(it)
        }
    }
}

private fun Lifecycle.runOnForeground(action: () -> Unit) {
    if (currentState == Lifecycle.State.DESTROYED) {
        return
    }

    if (currentState == Lifecycle.State.RESUMED) {
        GlobalScope.launch(Dispatchers.Main) { action() }
        return
    } else {
        addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun onResume() {
                GlobalScope.launch(Dispatchers.Main) { action() }
                removeObserver(this)
            }
        })
    }
}
