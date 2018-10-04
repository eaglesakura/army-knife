package com.eaglesakura.firearm.app.channel

import android.content.pm.PackageManager
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Deprecated("Replace to firearm.channel.RuntimePermissionResult")
@Parcelize
data class RuntimePermissionResult internal constructor(
        /**
         * Requested all permissions.
         */
        val permissions: List<String>,

        /**
         * Granted all permissions.
         */
        val grantResults: List<Int>,

        /**
         * Should show request rationale permissions.
         */
        val shouldShowRationalePermissions: List<String>
) : Parcelable {

    val granted: Boolean
        get() = (status == Status.Granted)

    val denied: Boolean
        get() = (status == Status.Denied)

    /**
     * Permission check result.
     *
     * e.g.)
     *
     * when(permissionResult.status) {
     *      Granted -> // Permission All OK!
     *      ShowRationale -> // Permission error, and show rationale.
     *      else -> // Denied...
     * }
     */
    val status: Status
        get() {
            if (!shouldShowRationalePermissions.isEmpty()) {
                return Status.ShowRationale
            }

            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    return Status.Denied
                }
            }

            return Status.Granted
        }

    enum class Status {
        Granted,
        Denied,
        ShowRationale
    }
}