package com.eaglesakura.armyknife.android.hardware.camera

import com.eaglesakura.KtxTestCase
import com.eaglesakura.armyknife.android.hardware.camera.spec.CameraType
import com.eaglesakura.armyknife.junit.blockingTest
import com.eaglesakura.armyknife.junit.validate
import org.junit.Test

class CameraSpecTest : KtxTestCase() {

    @Test
    fun getSpecs() = blockingTest {
        val specs = CameraSpec.getSpecs(application, CameraApi.Default, CameraType.Back)
        specs.getJpegPictureSize(640, 480).also {
            it.width.validate {
                from(1)
                to(640)
            }
            it.height.validate {
                from(1)
                to(480)
            }
        }
    }
}