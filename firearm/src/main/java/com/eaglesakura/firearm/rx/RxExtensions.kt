package com.eaglesakura.firearm.rx

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.android.Main
import kotlinx.coroutines.experimental.launch


internal fun Lifecycle.runOnForeground(action: () -> Unit) {
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
