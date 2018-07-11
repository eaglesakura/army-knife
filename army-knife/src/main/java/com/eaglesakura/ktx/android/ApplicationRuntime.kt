@file:Suppress("MemberVisibilityCanBePrivate")

package com.eaglesakura.ktx.android

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.ConnectivityManager
import android.os.Build
import android.provider.Settings
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import com.eaglesakura.ktx.android.extensions.UIHandler

object ApplicationRuntime {

    /**
     * Robolectric runtime is true.
     */
    val ROBOLECTRIC: Boolean = try {
        Class.forName("org.robolectric.Robolectric") != null
    } catch (err: ClassNotFoundException) {
        false
    }

    /**
     * If this app debugging now,
     * This property returns true,
     */
    fun isDebug(context: Context): Boolean {
        return context.packageManager.getApplicationInfo(context.packageName, 0)?.let { appInfo ->
            return appInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE == ApplicationInfo.FLAG_DEBUGGABLE
        } ?: false
    }

    fun getVersionName(context: Context): String {
        return context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_ACTIVITIES)?.versionName
                ?: ""
    }

    fun getVersionCode(context: Context): Long {
        return context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_ACTIVITIES)?.versionCode?.toLong()
                ?: 0L
    }

    /**
     * When developer mode enabled on this device, This property return true.
     * But, API Level less than 17, This property always returns false.
     */
    fun isDeveloperModeDevice(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT < 17) {
            false
        } else {
            Settings.Secure.getInt(context.contentResolver, Settings.Global.ADB_ENABLED, 0) != 0
        }
    }

    fun nowOnUIThread(): Boolean {
        return Thread.currentThread() == UIHandler.looper.thread
    }

    @UiThread
    fun assertUIThread() {
        if (!nowOnUIThread()) {
            throw Error("Thread[${Thread.currentThread()}] is not UI")
        }
    }

    @JvmStatic
    @WorkerThread
    fun assertWorkerThread() {
        if (nowOnUIThread()) {
            throw Error("Thread[${Thread.currentThread()}] is UI")
        }
    }

    /**
     * This method returns true when android-device connected to network.
     */
    @SuppressLint("MissingPermission")
    fun isConnectedNetwork(context: Context): Boolean {
        val service = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return service.activeNetworkInfo?.isConnected ?: false
    }

    /**
     * 自分自身がTop Applicationとして起動している場合はtrue
     */
    fun isForegroundApplicationSelf(context: Context): Boolean {
        return context.packageName == getTopApplicationPackage(context)
    }

    /**
     * ランチャー一覧を取得する
     */
    fun listLauncherApplications(context: Context): List<ResolveInfo> {
        val pm = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        return pm.queryIntentActivities(intent, 0)
    }

    /**
     * インストールされているアプリのpackage名一覧を取得する
     */
    fun listInstalledApplications(context: Context): List<ApplicationInfo> {
        val pm = context.packageManager
        return pm.getInstalledApplications(0)
    }

    /**
     * トップに起動しているActivityのpackage nameを指定する
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

}