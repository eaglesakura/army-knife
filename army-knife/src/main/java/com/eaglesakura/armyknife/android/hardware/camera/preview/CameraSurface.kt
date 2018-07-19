package com.eaglesakura.armyknife.android.hardware.camera.preview

import android.graphics.SurfaceTexture
import android.os.Build
import android.view.Surface

import com.eaglesakura.armyknife.android.hardware.camera.spec.CaptureSize


class CameraSurface {
    private var nativeSurface: Surface? = null

    private var surfaceTexture: SurfaceTexture? = null

    constructor(surfaceTexture: SurfaceTexture) {
        this.surfaceTexture = surfaceTexture
    }

    constructor(nativeSurface: Surface) {
        this.nativeSurface = nativeSurface
    }

    fun getNativeSurface(previewSize: CaptureSize): Surface {
        surfaceTexture?.also { surfaceTexture ->
            if (Build.VERSION.SDK_INT >= 15) {
                surfaceTexture.setDefaultBufferSize(previewSize.width, previewSize.height)
            }

            if (nativeSurface == null) {
                nativeSurface = Surface(this.surfaceTexture)
            }
        }

        if (nativeSurface == null) {
            throw NullPointerException("nativeSurface == null")
        }

        return nativeSurface!!
    }


    fun getSurfaceTexture(previewSize: CaptureSize): SurfaceTexture {
        surfaceTexture?.also { surfaceTexture ->
            if (Build.VERSION.SDK_INT >= 15) {
                surfaceTexture.setDefaultBufferSize(previewSize.width, previewSize.height)
            }
        }
        return surfaceTexture!!
    }
}
