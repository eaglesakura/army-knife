package com.eaglesakura.armyknife.android.extensions

import com.google.android.gms.tasks.Task
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.android.Main
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import java.util.concurrent.TimeUnit
import kotlin.coroutines.experimental.coroutineContext

const val FETCH_STATUS_HAS_VALUES = 0x01 shl 6

const val FETCH_STATUS_FLAG_COMPLETED = 0x01 shl 1 or FETCH_STATUS_HAS_VALUES

/**
 * Fetch failed, but Firebase has values(old values.)
 */
const val FETCH_STATUS_FLAG_CACHED = 0x1 shl 2 or FETCH_STATUS_HAS_VALUES

/**
 * Fetch failed.
 */
const val FETCH_STATUS_FLAG_FAILED = 0x01 shl 3

/**
 * Activate failed,
 */
const val FETCH_STATUS_FLAG_ACTIVATE = 0x01 shl 5

val FirebaseRemoteConfig.fetchStatus: Int
    get() {
        val state = info?.lastFetchStatus ?: FETCH_STATUS_FLAG_COMPLETED

        return if (state == FirebaseRemoteConfig.LAST_FETCH_STATUS_SUCCESS) {
            FETCH_STATUS_HAS_VALUES or FETCH_STATUS_FLAG_CACHED
        } else {
            var flags = FETCH_STATUS_FLAG_FAILED or FETCH_STATUS_FLAG_ACTIVATE
            if (state != FirebaseRemoteConfig.LAST_FETCH_STATUS_NO_FETCH_YET) {
                // 一度は同期できているであろうと考えられる
                flags = flags or FETCH_STATUS_HAS_VALUES
            }
            flags
        }
    }

fun FirebaseRemoteConfig.setDebug(set: Boolean) {
    FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(set).build().let {
        setConfigSettings(it)
    }
}

fun FirebaseRemoteConfig.forceActivate(): Int {
    // fetchが裏で成功しているので、古い結果でactivateする
    return if (activateFetched()) {
        FETCH_STATUS_FLAG_COMPLETED or FETCH_STATUS_FLAG_CACHED
    } else {
        val state = info.lastFetchStatus
        var flags = FETCH_STATUS_FLAG_FAILED or FETCH_STATUS_FLAG_ACTIVATE
        if (state != FirebaseRemoteConfig.LAST_FETCH_STATUS_NO_FETCH_YET) {
            // 一度は同期できているであろうと考えられる
            flags = flags or FETCH_STATUS_HAS_VALUES
        }
        flags
    }
}

fun FirebaseRemoteConfig.fetch(cacheTime: Long, cacheTimeUnit: TimeUnit): Task<Void> {
    val cacheTimeSec =
            if (info.configSettings.isDeveloperModeEnabled) {
                0
            } else {
                cacheTimeUnit.toSeconds(cacheTime)
            }
    return fetch(cacheTimeSec)
}

/**
 * Fetch firebase-getRemoteProperty-config values.
 *
 * Firebase has fatal issue, Firebase SDK(9.x) do not callback on task finished time.
 * Resolve it, by value-polling.
 *
 * @see FETCH_STATUS_FLAG_ACTIVATE
 * @see FETCH_STATUS_FLAG_CACHED
 * @see FETCH_STATUS_FLAG_COMPLETED
 * @see FETCH_STATUS_FLAG_FAILED
 * @see FETCH_STATUS_HAS_VALUES
 */
suspend fun FirebaseRemoteConfig.fetchAndActivate(cacheTime: Long = 1, cacheTimeUnit: TimeUnit = TimeUnit.HOURS): Int {
    val task = GlobalScope.async(coroutineContext + Dispatchers.Main) { fetch(cacheTime, cacheTimeUnit) }.await()

    while (true) {
        if (task.isComplete) {
            return if (task.isSuccessful) {
                // fetch成功した
                if (activateFetched() || info.lastFetchStatus != FirebaseRemoteConfig.LAST_FETCH_STATUS_NO_FETCH_YET) {
                    FETCH_STATUS_FLAG_COMPLETED
                } else {
                    FETCH_STATUS_FLAG_FAILED or FETCH_STATUS_FLAG_ACTIVATE
                }
            } else {
                // 裏のステータスを見る
                forceActivate()
            }
        }
        delay(1)
    }
}