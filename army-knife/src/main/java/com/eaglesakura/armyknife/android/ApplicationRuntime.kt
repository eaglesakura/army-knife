@file:Suppress("MemberVisibilityCanBePrivate")

package com.eaglesakura.armyknife.android

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.Service
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.ResolveInfo
import android.os.Build
import android.view.Surface
import android.view.WindowManager

/**
 * Utility for Application Runtime information.
 */
object ApplicationRuntime {

    /**
     * identifier of this process.
     *
     * @author @eaglesakura
     * @link https://github.com/eaglesakura/army-knife
     */
    val pid: Int
        get() = android.os.Process.myPid()

    /**
     * Kill self process.
     *
     * @author @eaglesakura
     * @link https://github.com/eaglesakura/army-knife
     */
    fun killSelf() {
        android.os.Process.killProcess(pid)
        while (true) {
            // spin lock.
        }
    }

    /**
     * Robolectric runtime is true.
     *
     * @author @eaglesakura
     * @link https://github.com/eaglesakura/army-knife
     */
    val ROBOLECTRIC: Boolean = try {
        Class.forName("org.robolectric.Robolectric")
        true
    } catch (err: ClassNotFoundException) {
        false
    }

    /**
     * returns rotation of the device in degree.
     *
     * @author @eaglesakura
     * @link https://github.com/eaglesakura/army-knife
     */
    fun getDeviceRotateDegree(context: Context): Int {
        val surfaceRotation =
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.rotation
        return when (surfaceRotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> throw IllegalStateException("rotate[$surfaceRotation] is not supported")
        }
    }

    /**
     * If this process is foreground now then returns true.
     *
     * @author @eaglesakura
     * @link https://github.com/eaglesakura/army-knife
     */
    fun isForegroundApplicationSelf(context: Context): Boolean {
        return context.packageName == getTopApplicationPackage(context)
    }

    /**
     * Returns launcher objects.
     */
    @Deprecated("will be delete this.")
    fun listLauncherApplications(context: Context): List<ResolveInfo> {
        val pm = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        return pm.queryIntentActivities(intent, 0)
    }

    /**
     * Returns installed applications.
     */
    @Deprecated("will be delete this.")
    fun listInstalledApplications(context: Context): List<ApplicationInfo> {
        val pm = context.packageManager
        return pm.getInstalledApplications(0)
    }

    /**
     * Returns an running application on top activity of this device.
     */
    @SuppressLint("WrongConstant")
    fun getTopApplicationPackage(context: Context): String {
        if (Build.VERSION.SDK_INT >= 22) {
            val usm = context.getSystemService("usagestats") as UsageStatsManager
            val time = System.currentTimeMillis()
            val events = usm.queryEvents(time - 1000 * 60 * 60, time)
            if (events != null && events.hasNextEvent()) {
                val app = android.app.usage.UsageEvents.Event()
                var lastAppTime: Long = 0
                var packageName: String? = null
                while (events.hasNextEvent()) {
                    events.getNextEvent(app)
                    if (app.timeStamp > lastAppTime && app.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                        packageName = app.packageName
                        lastAppTime = app.timeStamp
                    }
                }

                if (!packageName.isNullOrEmpty()) {
                    return packageName!!
                }
            }
        } else {
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val processes = activityManager.runningAppProcesses
            for (info in processes) {
                if (info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return if (info.importanceReasonComponent != null) {
                        info.importanceReasonComponent.packageName
                    } else {
                        info.pkgList[0]
                    }
                }
            }
        }
        return context.packageName
    }

    /**
     * This method returns true when a "clazz" service is running on this device.
     */
    fun <T : Service> isServiceRunning(context: Context, clazz: Class<T>): Boolean {
        try {
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val services = activityManager.getRunningServices(Integer.MAX_VALUE)
            for (info in services) {
                if (clazz.name == info.service.className) {
                    // 一致するクラスが見つかった
                    return true
                }
            }
            return false
        } catch (e: Exception) {

        }

        return false
    }

}