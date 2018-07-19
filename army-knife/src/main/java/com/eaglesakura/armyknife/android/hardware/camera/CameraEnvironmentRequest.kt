package com.eaglesakura.armyknife.android.hardware.camera

import com.eaglesakura.armyknife.android.hardware.camera.spec.FlashMode
import com.eaglesakura.armyknife.android.hardware.camera.spec.FocusMode
import com.eaglesakura.armyknife.android.hardware.camera.spec.Scene
import com.eaglesakura.armyknife.android.hardware.camera.spec.WhiteBalance

data class CameraEnvironmentRequest(
        val focusMode: FocusMode = FocusMode.SETTING_AUTO,

        val scene: Scene = Scene.SETTING_AUTO,

        val whiteBalance: WhiteBalance = WhiteBalance.SETTING_AUTO,

        val flashMode: FlashMode = FlashMode.SETTING_AUTO
)