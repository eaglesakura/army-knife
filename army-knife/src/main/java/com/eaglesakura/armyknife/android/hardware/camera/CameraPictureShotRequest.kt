package com.eaglesakura.armyknife.android.hardware.camera

import com.eaglesakura.armyknife.android.hardware.camera.spec.CaptureFormat
import com.eaglesakura.armyknife.android.hardware.camera.spec.CaptureSize

import androidx.annotation.FloatRange

data class CameraPictureShotRequest(
        val captureSize: CaptureSize,

        val format: CaptureFormat = CaptureFormat.Jpeg,

        @get:FloatRange(from = -90.0, to = 90.0)
        val latitude: Double? = null,

        @get:FloatRange(from = -180.0, to = 180.0)
        val longitude: Double? = null
) {
    val hasLocation: Boolean
        get() = latitude != null && longitude != null
}
