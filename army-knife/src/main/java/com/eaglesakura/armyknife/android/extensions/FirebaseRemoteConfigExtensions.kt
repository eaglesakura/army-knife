@file:Suppress("unused")

package com.eaglesakura.armyknife.android.extensions

import com.google.android.gms.tasks.Task
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import java.util.concurrent.TimeUnit

@Deprecated("replace to developerMode", ReplaceWith("developerMode"))
fun FirebaseRemoteConfig.setDebug(set: Boolean) {
    FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(set).build().let {
        setConfigSettings(it)
    }
}

/**
 * Developer Mode option.
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
var FirebaseRemoteConfig.developerMode
    get() = info.configSettings.isDeveloperModeEnabled
    set(value) {
        setConfigSettings(
            FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(value).build()
        )
    }

/**
 * fetch remote configs from firebase.
 *
 * e.g.)
 * FirebaseRemoteConfig.getInstance().fetch(12, TimeUnit.HOURS)
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
fun FirebaseRemoteConfig.fetch(cacheTime: Long, cacheTimeUnit: TimeUnit): Task<Void> {
    val cacheTimeSec =
        if (info.configSettings.isDeveloperModeEnabled) {
            0
        } else {
            cacheTimeUnit.toSeconds(cacheTime)
        }
    return fetch(cacheTimeSec)
}
