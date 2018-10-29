package com.eaglesakura.firearm.app.channel

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.annotation.CheckResult
import androidx.annotation.UiThread
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.eaglesakura.armyknife.android.extensions.assertUIThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.launch

/**
 * RuntimePermission Dispatcher with Channel.
 */
@Deprecated("Replace to firearm.channel.RuntimePermissionDispatcher")
class RuntimePermissionDispatcher(
        private val getContext: () -> Context,
        private val requestPermissions: (permissions: Array<String>, requestCode: Int) -> Unit,
        private val shouldShowRequestPermissionRationale: (permission: String) -> Boolean,
        private val registry: ChannelRegistry
) {

    constructor(fragment: Fragment, registry: ChannelRegistry) :
            this(
                    { fragment.context!! },
                    fragment::requestPermissions,
                    fragment::shouldShowRequestPermissionRationale,
                    registry
            )

    constructor(activity: Activity, registry: ChannelRegistry) :
            this(
                    { activity },
                    { permissions, requestCode -> ActivityCompat.requestPermissions(activity, permissions, requestCode) },
                    { permission -> ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) },
                    registry
            )

    private fun makeRequestCode(permissions: Collection<String>) = permissions.toHashSet().toString().hashCode() and 0x0000FFFF

    private fun makeKey(requestCode: Int) = "permission@$requestCode"

    /**
     * Show runtime permission dialog, with await.
     * You shouldn't keep channel, and "close()" channel.
     */
    suspend fun requestPermissionsWithResult(permissions: Collection<String>): RuntimePermissionResult {
        return requestPermissions(permissions).consume {
            receive()
        }
    }

    /**
     * Show runtime permission dialog.
     *
     * You should check result channel, and close().
     * Call this function from coroutines? can replace to "requestPermissionsWithResult()" function.
     */
    @CheckResult
    @UiThread
    fun requestPermissions(permissions: Collection<String>): Channel<RuntimePermissionResult> {
        assertUIThread()

        if (permissions.isEmpty()) {
            throw IllegalArgumentException("Permission not required")
        }

        // check permissions
        val requestCode = makeRequestCode(permissions)
        val key = makeKey(requestCode)
        val context = getContext()

        val requestPermissions = mutableListOf<String>()
        for (permission in permissions) {
            when (ContextCompat.checkSelfPermission(context, permission)) {
                // you have permission!
                PackageManager.PERMISSION_GRANTED -> {
                    Log.d(TAG, "has permission[$permission]")
                }
                else -> {
                    // Check permission
                    Log.d(TAG, "request permission[$permission]")
                    requestPermissions.add(permission)
                }
            }
        }

        val result = registry.register(key, Channel<RuntimePermissionResult>())
        GlobalScope.launch(Dispatchers.Main) {
            if (requestPermissions.isEmpty()) {
                // have all permissions
                Log.d(TAG, "you have all permissions $permissions")
                result.send(
                        RuntimePermissionResult(
                                permissions.toList(),
                                permissions.map { PackageManager.PERMISSION_GRANTED },
                                emptyList()
                        )
                )
            } else {
                // request now.
                requestPermissions(permissions.toTypedArray(), requestCode)
            }
        }
        return result
    }

    /**
     * Should call this method on "onRequestPermissionsResult" from Fragment or Activity.
     */
    @UiThread
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        assertUIThread()

        val key = makeKey(requestCode)
        try {
            val channel = registry.get<RuntimePermissionResult>(key)

            val shouldShowRationalePermissions = mutableListOf<String>()
            for (permission in permissions) {
                if (shouldShowRequestPermissionRationale(permission)) {
                    shouldShowRationalePermissions.add(permission)
                }
            }

            GlobalScope.launch(Dispatchers.Main) {
                channel.send(
                        RuntimePermissionResult(
                                permissions.toList(),
                                grantResults.toList(),
                                shouldShowRationalePermissions
                        ))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Channel not found, Activity or Fragment was might destroyed.")
            e.printStackTrace()
        }
    }

    /**
     * Returns runtime permission status, just now.
     */
    fun getRuntimePermissionStatus(permissions: Collection<String>): RuntimePermissionResult {

        val context = getContext()
        val shouldShowRationalePermissions = mutableListOf<String>()
        for (permission in permissions) {
            if (shouldShowRequestPermissionRationale(permission)) {
                shouldShowRationalePermissions.add(permission)
            }
        }

        return RuntimePermissionResult(
                permissions.toList(),
                permissions.map { permission -> ActivityCompat.checkSelfPermission(context, permission) },
                shouldShowRationalePermissions
        )
    }

    companion object {
        private val TAG = RuntimePermissionDispatcher::class.java.simpleName
    }
}
