package com.eaglesakura.ktx.android.extensions

import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Result
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.launch

suspend fun <T> Task<T>.awaitWithSuspend(): Task<T> {
    val channel = Channel<Unit>()
    addOnCompleteListener {
        launch(UI) { channel.send(Unit) }
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
        launch(UI) { channel.send(it) }
    }
    return channel.receive()
}
