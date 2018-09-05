package com.eaglesakura.armyknife.runtime.extensions

import androidx.core.os.CancellationSignal


/**
 * Cancellation-signal function.
 * You want to cancel, this function returns "true".
 */
typealias CancelCallback = () -> Boolean

/**
 * CancellationSignal to CancelSignal.
 * This function supports only CancellationSignal in "androidx".
 */
fun CancellationSignal.asCancelCallback(): CancelCallback {
    return fun(): Boolean {
        return isCanceled
    }
}