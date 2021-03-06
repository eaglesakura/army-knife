package com.eaglesakura.oneshotlivedata

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Deprecated("Use firearm.RxStream")
fun <T> newOneshotObserver(block: (data: T) -> Unit): Observer<DataState<T>> {
    return Observer {
        @Suppress("UNCHECKED_CAST")
        val data = it?.getAny() as? T ?: return@Observer
        block(data)
    }
}

@Deprecated("Use firearm.RxStream")
fun <T> newOneshotObserverWithForeground(owner: LifecycleOwner, block: (data: T) -> Unit): Observer<DataState<T>> {
    return newOneshotObserver<T> {
        runOnForeground(owner.lifecycle) { block(it) }
    }
}

@Deprecated("Use firearm.RxStream")
fun newEventObserver(block: (event: Event) -> Unit): Observer<EventDataState> {
    return Observer {
        val event = it?.get<Event>() ?: return@Observer
        block(event)
    }
}

@Deprecated("Use firearm.RxStream")
fun newEventObserverWithForeground(owner: LifecycleOwner, block: (event: Event) -> Unit): Observer<EventDataState> {
    return newEventObserver {
        runOnForeground(owner.lifecycle) { block(it) }
    }
}

internal fun runOnForeground(lifecycle: Lifecycle, action: () -> Unit) {
    if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
        return
    }

    if (lifecycle.currentState == Lifecycle.State.RESUMED) {
        GlobalScope.launch(Dispatchers.Main) { action() }
        return
    } else {
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun onResume() {
                GlobalScope.launch(Dispatchers.Main) { action() }
                lifecycle.removeObserver(this)
            }
        })
    }
}
