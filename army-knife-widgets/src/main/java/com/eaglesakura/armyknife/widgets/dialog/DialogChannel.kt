package com.eaglesakura.armyknife.widgets.dialog

import android.app.Dialog
import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import com.eaglesakura.armyknife.android.extensions.show
import com.eaglesakura.armyknife.runtime.extensions.send
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

typealias DialogChannel = Channel<DialogResult>

/**
 * User dialog clicked result.
 */
enum class DialogResult {
    /**
     * Dismiss with Positive button.
     */
    Positive,

    /**
     * Dismiss with Negative button.
     */
    Negative,

    /**
     * Dismiss with Neutral button.
     */
    Neutral,

    /**
     * Dismiss with canceled.
     */
    Cancel,

    /**
     * Dismissed dialog.
     */
    Dismiss
}

/**
 * Dialog to channel.
 *
 * e.g.)
 * val channel = dialog.toChannel()
 * val result = channel.receive() // blocking until dialog dismiss.
 * // do something.
 */
fun Dialog.toChannel(): DialogChannel {
    val result = Channel<DialogResult>()
    setOnCancelListener {
        result.send(Dispatchers.Main, DialogResult.Cancel)
    }
    setOnDismissListener {
        GlobalScope.launch(Dispatchers.Main) {
            result.send(DialogResult.Dismiss)
            result.close()
        }
    }
    return result
}

/**
 * AlertDialog to channel.
 *
 * Don't set positive/neutral/negative button text by AlertDialog.Builder.
 *
 * e.g.)
 * val channel = buildAlertDialogChannel( /* arguments */ ) {
 *      setPositiveButton("Positive")
 *      setNegativeButton("Negative")
 * }
 * val result = channel.receive() // blocking until dialog dismiss.
 * // do something.
 */
fun buildAlertDialogChannel(
    context: Context,
    builder: AlertDialog.Builder = AlertDialog.Builder(context),
    block: AlertDialogChannelBuilder.() -> Unit
): Channel<DialogResult> {
    return AlertDialogChannelBuilder(builder).also(block).show()
}

class AlertDialogChannelBuilder internal constructor(
    @Suppress("MemberVisibilityCanBePrivate")
    val builder: AlertDialog.Builder
) {
    private var lifecycle: Lifecycle? = null

    private var positiveButtonText: CharSequence? = null
    private var negativeButtonText: CharSequence? = null
    private var neutralButtonText: CharSequence? = null

    /**
     * Get dialog's context.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    val context: Context
        get() = builder.context


    /**
     * Get string from resource.
     * delegate to Context.getString()
     */
    fun getString(@StringRes resId: Int): String {
        return context.getString(resId)
    }

    /**
     * Get string from resource.
     * delegate to Context.getString()
     */
    fun getString(@StringRes resId: Int, vararg arguments: Any): String {
        return context.getString(resId, *arguments)
    }

    /**
     * Dialog title.
     */
    var title: CharSequence = ""
        set(value) {
            builder.setTitle(value)
            field = title
        }

    /**
     * Dialog message.
     */
    var message: CharSequence = ""
        set(value) {
            builder.setMessage(value)
            field = value
        }


    /**
     * Cancelable flag.
     */
    var cancelable: Boolean = false
        set(value) {
            builder.setCancelable(value)
            field = value
        }

    /**
     * Positive button text.
     */
    var positiveButton: CharSequence
        get() = positiveButtonText!!
        set(value) {
            positiveButtonText = value
        }

    /**
     * Negative button text.
     */
    var negativeButton: CharSequence
        get() = negativeButtonText!!
        set(value) {
            negativeButtonText = value
        }

    /**
     * Negative button text.
     */
    var neutralButton: CharSequence
        get() = neutralButtonText!!
        set(value) {
            neutralButtonText = value
        }

    /**
     * Link lifecycle, then auto dismiss.
     */
    fun with(lifecycle: Lifecycle) {
        this.lifecycle = lifecycle
    }

    fun show(): Channel<DialogResult> {
        val channel = Channel<DialogResult>()
        positiveButtonText?.also { text ->
            builder.setPositiveButton(text) { _, _ ->
                channel.send(Dispatchers.Main, DialogResult.Positive)
            }
        }
        negativeButtonText?.also { text ->
            builder.setNegativeButton(text) { _, _ ->
                channel.send(Dispatchers.Main, DialogResult.Negative)
            }
        }
        neutralButtonText?.also { text ->
            builder.setNeutralButton(text) { _, _ ->
                channel.send(Dispatchers.Main, DialogResult.Neutral)
            }
        }
        builder.setOnCancelListener {
            channel.send(Dispatchers.Main, DialogResult.Cancel)
        }
        builder.setOnDismissListener {
            GlobalScope.launch(Dispatchers.Main) {
                channel.close()
            }
        }

        if (lifecycle != null) {
            builder.show(lifecycle!!)
        } else {
            builder.show()
        }

        return channel
    }
}
