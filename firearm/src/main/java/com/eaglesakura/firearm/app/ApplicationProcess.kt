package com.eaglesakura.firearm.app

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.eaglesakura.armyknife.android.extensions.debugMode
import com.eaglesakura.armyknife.android.hardware.DisplayInfo
import com.eaglesakura.armyknife.runtime.Random
import com.eaglesakura.firearm.app.ApplicationProcess.Companion.EVENT_APPLICATION_BACKGROUND
import com.eaglesakura.firearm.app.ApplicationProcess.Companion.EVENT_APPLICATION_FOREGROUND
import com.eaglesakura.oneshotlivedata.EventId
import com.eaglesakura.oneshotlivedata.EventStream
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
    var log: (msg: String) -> Unit = { msg ->
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
     * @see EVENT_APPLICATION_BACKGROUND
     * @see EVENT_APPLICATION_FOREGROUND
     */
    val event: EventStream = EventStream(EVENT_APPLICATION_BACKGROUND, EVENT_APPLICATION_FOREGROUND)

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

        log("Install Unique ID [${settings.installUniqueId}]")
        log("Process Unique ID [$processId]")
        log("VersionCode       [$oldVersionCode] -> [$versionCode]")
        log("VersionName       [$oldVersionName] -> [$versionName]")
        log("API Level         [$oldSdkInt] -> [$sdkInt]")

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

        log("========= Runtime Information =========")
        log("== Device ${Build.MODEL}")
        DisplayInfo.newInstance(application).also { displayInfo ->
            log("== Display ${displayInfo.diagonalRoundInch.major}.${displayInfo.diagonalRoundInch.minor} inch = ${displayInfo.deviceType.name}")
            log("==   Display [${displayInfo.widthPixel} x ${displayInfo.heightPixel}] pix")
            log("==   Display [%.1f x %.1f] dp".format(displayInfo.widthDp, displayInfo.heightDp))
            log("==   res/values-${displayInfo.dpi.name}")
            log("==   res/values-sw${displayInfo.smallestWidthDp}dp")
        }
        log("========= Runtime Information =========")
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

internal class ActivityCallbackImpl(private val event: EventStream) : Application.ActivityLifecycleCallbacks {

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
            event.setOneshot(EVENT_APPLICATION_FOREGROUND)
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
            event.setOneshot(EVENT_APPLICATION_BACKGROUND)
        }
    }

    override fun onActivityDestroyed(activity: Activity?) {
    }


}