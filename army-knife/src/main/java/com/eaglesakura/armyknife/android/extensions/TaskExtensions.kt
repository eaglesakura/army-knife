package com.eaglesakura.armyknife.android.extensions

import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Result
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.android.Main
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

suspend fun <T> Task<T>.awaitWithSuspend(): Task<T> {
    val channel = Channel<Unit>()
    addOnCompleteListener {
        GlobalScope.launch(Dispatchers.Main) {
            channel.send(Unit)
        }
    }
    addOnCanceledListener {
        channel.close(CancellationException())
    }
    channel.receive()
    return this
}

/**
 * キャンセルチェックを行ったうえで処理待ちを行う
 */
suspend fun <T : Result> PendingResult<T>.awaitWithSuspend(): T {
    val channel = Channel<T>()
    this.setResultCallback {
        GlobalScope.launch(Dispatchers.Main) { channel.send(it) }
    }
    return channel.receive()
}
