package com.eaglesakura.firearm.app

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.eaglesakura.armyknife.android.extensions.debugMode
import com.eaglesakura.armyknife.android.hardware.DisplayInfo
import com.eaglesakura.armyknife.android.reactivex.RxStream
import com.eaglesakura.armyknife.runtime.Random
import com.eaglesakura.firearm.app.ApplicationProcess.Companion.EVENT_APPLICATION_BACKGROUND
import com.eaglesakura.firearm.app.ApplicationProcess.Companion.EVENT_APPLICATION_FOREGROUND
import com.eaglesakura.firearm.event.EventId
import java.lang.ref.WeakReference

/**
 * ApplicationProcess has system and process data.
 *
 * Caution, This constructor should be calls in UI Thread.
 */
class ApplicationProcess(val application: Application) {

    /**
     * Logger function.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    var console: (msg: String) -> Unit = { msg ->
        Log.d(javaClass.simpleName, msg)
    }

    @Suppress("MemberVisibilityCanBePrivate")
    val processId: String = Random.smallString()

    /**
     * Install-level unique id.
     *
     * This value has been generated on first boot.
     * It is not refreshable, Length of string is 32, Characters of string are [a-z,A-Z,0-9].
     */
    val installId: String
        get() = settings.installUniqueId

    private val settings: SystemSettings

    /**
     * Event for Application.
     * @see EVENT_APPLICATION_BACKGROUND
     * @see EVENT_APPLICATION_FOREGROUND
     */
    val event = RxStream<EventId> { event ->
        when (event) {
            EVENT_APPLICATION_BACKGROUND, EVENT_APPLICATION_FOREGROUND -> true
            else -> false
        }
    }

    private val activityCallback = ActivityCallbackImpl(event)

    private lateinit var _versionContext: VersionContext

    val versionContext: VersionContext
        get() = _versionContext

    init {
        application.registerActivityLifecycleCallbacks(activityCallback)
        settings = SystemSettings(application)
        refreshSettings()
        printDeviceInfo()
    }

    private fun refreshSettings() {
        if (settings.installUniqueId.isEmpty()) {
            settings.transaction {
                settings.installUniqueId = Random.string()
            }
        }

        val oldVersionCode = settings.lastBootedAppVersionCode
        val oldVersionName = settings.lastBootedAppVersionName
        val oldSdkInt = settings.lastBootedApiLevel
        val versionName = application.packageManager.getPackageInfo(application.packageName, 0x00).versionName!!
        val versionCode = application.packageManager.getPackageInfo(application.packageName, 0x00).let { packageInfo ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode
            } else {
                @Suppress("DEPRECATION")
                packageInfo.versionCode.toLong()
            }
        }
        val sdkInt = Build.VERSION.SDK_INT

        console("Install Unique ID [${settings.installUniqueId}]")
        console("Process Unique ID [$processId]")
        console("VersionCode       [$oldVersionCode] -> [$versionCode]")
        console("VersionName       [$oldVersionName] -> [$versionName]")
        console("API Level         [$oldSdkInt] -> [$sdkInt]")

        _versionContext = VersionContext(oldVersionName, oldVersionCode, versionName, versionCode, Build.VERSION.SDK_INT)
        settings.transaction {
            settings.lastBootedAppVersionCode = versionCode
            settings.lastBootedAppVersionName = versionName
            settings.lastBootedApiLevel = sdkInt
        }
    }

    private fun printDeviceInfo() {
        if (!application.debugMode) {
            return
        }

        console("========= Runtime Information =========")
        console("== Device ${Build.MODEL}")
        DisplayInfo.newInstance(application).also { displayInfo ->
            console("== Display ${displayInfo.diagonalRoundInch.major}.${displayInfo.diagonalRoundInch.minor} inch = ${displayInfo.deviceType.name}")
            console("==   Display [${displayInfo.widthPixel} x ${displayInfo.heightPixel}] pix")
            console("==   Display [%.1f x %.1f] dp".format(displayInfo.widthDp, displayInfo.heightDp))
            console("==   res/values-${displayInfo.dpi.name}")
            console("==   res/values-sw${displayInfo.smallestWidthDp}dp")
        }
        console("========= Runtime Information =========")
    }

    companion object {
        /**
         * Application move to Foreground.
         */
        @JvmStatic
        val EVENT_APPLICATION_FOREGROUND = EventId("EVENT_APPLICATION_FOREGROUND")

        /**
         * Application move to background.
         */
        @JvmStatic
        val EVENT_APPLICATION_BACKGROUND = EventId("EVENT_APPLICATION_BACKGROUND")
    }
}

private class ActivityCallbackImpl(private val event: RxStream<EventId>) : Application.ActivityLifecycleCallbacks {

    /**
     * Foregroundとして扱われているActivity
     */
    private var foregroundActivity: WeakReference<Activity>? = null

    override fun onActivityStarted(activity: Activity?) {
        var moveToForeground = false
        if (foregroundActivity == null || foregroundActivity?.get() == null) {
            // フォアグラウンドに移動した
            moveToForeground = true
        }

        foregroundActivity = WeakReference<Activity>(activity)
        if (moveToForeground) {
            event.next(EVENT_APPLICATION_FOREGROUND)
        }
    }


    override fun onActivityCreated(activity: Activity?, state: Bundle?) {
    }

    override fun onActivityResumed(activity: Activity?) {
    }

    override fun onActivityPaused(activity: Activity?) {
    }

    override fun onActivitySaveInstanceState(activity: Activity?, state: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity?) {
        // 自身がForegroundであったのなら、参照を排除する
        if (foregroundActivity?.get() === activity) {
            foregroundActivity = null
        }

        // Activityがなくなったら通知
        if (foregroundActivity == null) {
            event.next(EVENT_APPLICATION_BACKGROUND)
        }
    }

    override fun onActivityDestroyed(activity: Activity?) {
    }


}