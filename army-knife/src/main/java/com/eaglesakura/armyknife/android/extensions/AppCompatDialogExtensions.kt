package com.eaglesakura.armyknife.android.extensions

import android.app.Dialog
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle

/**
 * AlertDialog link to Lifecycle.
 * When lifecycle on destroy, then dismiss this dialog.
 */
fun AlertDialog.Builder.show(lifecycle: Lifecycle): AlertDialog {
    return show().also {
        it.with(lifecycle)
    }
}

/**
 * Dialog link to Lifecycle.
 * When lifecycle on destroy, then dismiss this dialog.
 */
fun Dialog.with(lifecycle: Lifecycle) {
    val dialog = this
    lifecycle.subscribe {
        if (it == Lifecycle.Event.ON_DESTROY) {
            dialog.forceDismiss()
        }
    }
}

private fun Dialog.forceDismiss() {
    try {
        if (isShowing) {
            dismiss()
        }
    } catch (e: Throwable) {
    }
}