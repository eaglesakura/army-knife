package com.eaglesakura.armyknife.android.extensions

import android.app.Notification
import android.app.Service
import androidx.core.app.NotificationCompat

/**
 * startForeground with Builder.
 *
 * @param id Foreground service id.
 * @param channelId channel id for Android O or later.
 * @param block Build notification block.
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
fun Service.startForeground(
    id: Int,
    channelId: String,
    block: NotificationCompat.Builder.() -> Unit
): Notification {
    val notification = NotificationCompat.Builder(this, channelId).apply(block).build()!!
    startForeground(id, notification)
    return notification
}