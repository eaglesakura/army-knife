package com.eaglesakura.armyknife.android.extensions

import android.graphics.Canvas
import android.graphics.Rect
import android.view.Surface
import android.view.SurfaceHolder
import android.view.TextureView
import com.eaglesakura.armyknife.android.view.SurfaceTextureHolder

/**
 * convert a TextureView to SurfaceHolder.
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
fun TextureView.toSurfaceHolder(): SurfaceTextureHolder = SurfaceTextureToSurfaceHolderBridge(this)

internal class SurfaceTextureToSurfaceHolderBridge(
    private val textureView: TextureView
) : SurfaceTextureHolder {

    private var surface: Surface? = Surface(textureView.surfaceTexture)

    private val surfaceSize = Rect(0, 0, textureView.width, textureView.height)

    override fun setType(type: Int) {
    }

    override fun getSurface(): Surface? {
        return surface
    }

    override fun setSizeFromLayout() {
    }

    override fun lockCanvas(): Canvas? {
        return null
    }

    override fun lockCanvas(dirty: Rect?): Canvas? {
        return null
    }

    override fun setFixedSize(width: Int, height: Int) {
    }

    override fun removeCallback(callback: SurfaceHolder.Callback?) {
    }

    override fun isCreating(): Boolean {
        return surface != null
    }

    override fun addCallback(callback: SurfaceHolder.Callback?) {
    }

    override fun setFormat(format: Int) {
    }

    override fun setKeepScreenOn(screenOn: Boolean) {
    }

    override fun unlockCanvasAndPost(canvas: Canvas?) {
    }

    override fun getSurfaceFrame(): Rect {
        return surfaceSize
    }

    override fun onResizeTexture(width: Int, height: Int) {
        surfaceSize.set(0, 0, width, height)
    }

    override fun close() {
        surface = null
    }
}