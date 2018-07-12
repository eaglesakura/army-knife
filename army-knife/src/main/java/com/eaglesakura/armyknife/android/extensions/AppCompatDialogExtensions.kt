package com.eaglesakura.armyknife.android.extensions

import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle

/**
 * コードブロックの追加を簡素化するためのUtil
 */
fun AlertDialog.Builder.positiveButton(text: CharSequence, block: () -> Unit): AlertDialog.Builder {

    this.setPositiveButton(text) { _, _ ->
        block()
    }
    return this
}

/**
 * コードブロックの追加を簡素化するためのUtil
 */
fun AlertDialog.Builder.positiveButton(@StringRes textId: Int, block: () -> Unit): AlertDialog.Builder {
    return positiveButton(context.getText(textId), block)
}

/**
 * コードブロックの追加を簡素化するためのUtil
 */
fun AlertDialog.Builder.negativeButton(text: CharSequence, block: () -> Unit): AlertDialog.Builder {
    this.setNegativeButton(text) { _, _ ->
        block()
    }
    return this
}

/**
 * コードブロックの追加を簡素化するためのUtil
 */
fun AlertDialog.Builder.negativeButton(@StringRes textId: Int, block: () -> Unit): AlertDialog.Builder {
    return negativeButton(context.getText(textId), block)
}

/**
 * ライフサイクルをチェックして自動的に AlertDialog.dismiss() を行うUtil.
 */
fun AlertDialog.Builder.show(lifecycle: Lifecycle): AlertDialog {
    val dialog = this.show()
    lifecycle.subscribe {
        if (it == Lifecycle.Event.ON_DESTROY) {
            try {
                if (dialog.isShowing) {
                    dialog.dismiss()
                }
            } catch (err: Throwable) {
                // drop error
            }
        }
    }
    return dialog
}