package com.eaglesakura.ktx.oneshotlivedata

import androidx.lifecycle.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

fun <T> newOneshotObserver(block: (data: T) -> Unit): Observer<DataState<T>> {
    return Observer {
        @Suppress("UNCHECKED_CAST")
        val data = it?.getAny() as? T ?: return@Observer
        block(data)
    }
}

fun <T> newOneshotObserverWithForeground(owner: LifecycleOwner, block: (data: T) -> Unit): Observer<DataState<T>> {
    return newOneshotObserver<T> {
        runOnForeground(owner.lifecycle) { block(it) }
    }
}

fun newEventObserver(block: (event: Event) -> Unit): Observer<EventDataState> {
    return Observer {
        val event = it?.get<Event>() ?: return@Observer
        block(event)
    }
}

fun newEventObserverWithForeground(owner: LifecycleOwner, block: (event: Event) -> Unit): Observer<EventDataState> {
    return newEventObserver {
        runOnForeground(owner.lifecycle) { block(it) }
    }
}

private fun runOnForeground(lifecycle: Lifecycle, action: () -> Unit) {
    if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
        return
    }

    if (lifecycle.currentState == Lifecycle.State.RESUMED) {
        launch(UI) { action() }
        return
    } else {
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun onResume() {
                launch(UI) { action() }
                lifecycle.removeObserver(this)
            }
        })
    }
}
