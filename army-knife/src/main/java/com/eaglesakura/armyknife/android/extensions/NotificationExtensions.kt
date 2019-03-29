package com.eaglesakura.armyknife.android.extensions

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService

/**
 * A NotificationChannel insert to NotificationManager on Android O or later.
 *
 * e.g.)
 *
 * createNotificationChannel(context) {
 *      NotificationChannel("default", "default notification", NotificationManager.IMPORTANCE_DEFAULT)
 * }
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
inline fun createNotificationChannel(context: Context, block: () -> NotificationChannel) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = block()
        val notificationManager: NotificationManager = context.getSystemService()!!
        notificationManager.createNotificationChannel(notificationChannel)
    }
}

/**
 * Build notification wrapper.
 *
 * e.g.)
 *
 * notificationToStatusBar(context, "default", 0x3103) {
 *      // build notification by NotificationCompat.Builder.()
 * }
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
inline fun notificationToStatusBar(
    context: Context,
    channelId: String,
    notificationId: Int,
    block: NotificationCompat.Builder.() -> Unit
): Notification {
    val notification = NotificationCompat.Builder(context, channelId).apply(block).build()!!
    val notificationManager = NotificationManagerCompat.from(context)
    notificationManager.notify(notificationId, notification)
    return notification
}

/**
 * Build notification wrapper.
 *
 * e.g.)
 *
 * notificationToStatusBar(context, "default", "default", 0x3103) {
 *      // build notification by NotificationCompat.Builder.()
 * }
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
inline fun notificationToStatusBar(
    context: Context,
    channelId: String,
    tag: String,
    notificationId: Int,
    block: NotificationCompat.Builder.() -> Unit
): Notification {
    val notification = NotificationCompat.Builder(context, channelId)
        .apply {
            setChannelId(channelId)
        }
        .apply(block)
        .build()!!
    val notificationManager = NotificationManagerCompat.from(context)
    notificationManager.notify(tag, notificationId, notification)
    return notification
}
