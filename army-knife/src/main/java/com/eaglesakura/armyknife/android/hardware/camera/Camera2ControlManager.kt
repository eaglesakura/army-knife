package com.eaglesakura.armyknife.android.hardware.camera

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Build
import android.view.Surface
import com.eaglesakura.armyknife.android.extensions.AsyncHandler
import com.eaglesakura.armyknife.android.hardware.camera.error.CameraAccessFailedException
import com.eaglesakura.armyknife.android.hardware.camera.error.CameraException
import com.eaglesakura.armyknife.android.hardware.camera.error.CameraSecurityException
import com.eaglesakura.armyknife.android.hardware.camera.preview.CameraSurface
import com.eaglesakura.armyknife.android.hardware.camera.spec.CaptureFormat
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.android.HandlerContext
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import okhttp3.internal.Util
import java.util.ArrayList


private val conrolHandler: AsyncHandler = AsyncHandler.newInstance("camera-control")

private val controlDispatcher: CoroutineDispatcher = HandlerContext(conrolHandler, "camera-dispatcher")

private val processingHandler: AsyncHandler = AsyncHandler.newInstance("camera-processing")

@SuppressLint("MissingPermission")
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class Camera2ControlManager(
        context: Context,
        private val api: CameraApi,
        private val request: CameraConnectRequest) : CameraControlManager() {

    private val mCharacteristics: CameraCharacteristics

    private var camera: CameraDevice? = null

    private val spec: Camera2SpecImpl = Camera2SpecImpl(context)

    /**
     * 撮影用セッション
     *
     * MEMO: 基本的に使いまわさなければ撮影とプレビューを両立できない
     */
    private var captureSession: CameraCaptureSession? = null

    private var previewCaptureRequest: CaptureRequest.Builder? = null

    /**
     * プレビュー用バッファ
     */
    private var previewSurface: CameraSurface? = null

    /**
     * 撮影用バッファ
     */
    private var imageReader: ImageReader? = null

    /**
     * 接続時に確定されたプレビューリクエスト
     */
    private var previewRequest: CameraPreviewRequest? = null

    /**
     * 接続時に確定された撮影リクエスト
     */
    private var pictureShotRequest: CameraPictureShotRequest? = null

    private var mFlags: Int = 0

    override val supportApi: CameraApi
        get() = CameraApi.Camera2

    override val isPreviewNow: Boolean
        get() = mFlags.and(FLAG_NOW_PREVIEW) == FLAG_NOW_PREVIEW

    override val connected: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override suspend fun connect(previewSurface: CameraSurface?, previewRequest: CameraPreviewRequest?, shotRequest: CameraPictureShotRequest?): Unit = withContext(controlDispatcher) {
        val channel = Channel<CameraDevice>()

        try {
            this.pictureShotRequest = shotRequest
            this.previewRequest = previewRequest
            this.previewSurface = previewSurface

            spec.cameraManager.openCamera(spec.cameraId, object : CameraDevice.StateCallback() {
                override fun onOpened(cameraDevice: CameraDevice) {
                    launch(controlDispatcher) {
                        channel.send(cameraDevice)
                    }
                }

                override fun onDisconnected(cameraDevice: CameraDevice) {
                    cameraDevice.close()
                    camera = null
                }

                override fun onError(cameraDevice: CameraDevice, error: Int) {
                    launch(controlDispatcher) {
                        if (error == CameraDevice.StateCallback.ERROR_CAMERA_IN_USE) {
                            // すでに使われている
                            channel.send(cameraDevice)
                        } else {
                            channel.cancel(CameraSecurityException("Error[$error]"))
                            cameraDevice.close()
                            camera = null
                        }
                    }
                }
            }, conrolHandler)

            camera = channel.receive()
        } catch (err: CameraAccessException) {
            throw CameraAccessFailedException(err)
        }
    }

    override suspend fun disconnect() {
        withContext(controlDispatcher) {
            if (!connected) {
                throw IllegalStateException("not connencted")
            }

            try {
                stopPreviewImpl()
            } catch (e: Exception) {

            }

            imageReader?.close()
            camera?.close()

            imageReader = null
            camera = null
            pictureShotRequest = null
            previewRequest = null
            previewSurface = null
        }
    }

    @Synchronized
    @Throws(CameraException::class)
    private suspend fun getSession(): CameraCaptureSession {
        if (camera == null) {
            throw CameraAccessFailedException("connect() not called")
        }

        if (captureSession != null) {
            return captureSession!!
        }

        val surfaces = ArrayList<Surface>()

        previewRequest?.also { cameraPreviewRequest ->
            surfaces.add(previewSurface!!.getNativeSurface(cameraPreviewRequest.previewSize))
        }

        pictureShotRequest?.also { cameraPictureShotRequest ->
            imageReader = ImageReader.newInstance(
                    cameraPictureShotRequest.captureSize.width, cameraPictureShotRequest.captureSize.height,
                    if (cameraPictureShotRequest.format === CaptureFormat.Raw) ImageFormat.RAW_SENSOR else ImageFormat.JPEG,
                    2
            )
            surfaces.add(imageReader!!.surface)
        }

        val channel = Channel<CameraCaptureSession>()
        try {
            camera!!.createCaptureSession(surfaces, object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(session: CameraCaptureSession) {
                    launch(controlDispatcher) {
                        channel.send(session)
                    }
                }

                override fun onConfigureFailed(session: CameraCaptureSession) {
                    channel.close(CameraException("Session create failed"))
                }
            }, conrolHandler)
            return channel.receive()
        } catch (e: CameraAccessException) {
            throw CameraAccessFailedException(e)
        }
    }

    override suspend fun startPreview(env: CameraEnvironmentRequest?) {

        try {
            // セッションを生成する
            val previewSession = getSession()

            // プレビューを開始する
            if (previewCaptureRequest == null) {
                val builder = newCaptureRequest(env, CameraDevice.TEMPLATE_PREVIEW)
                builder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START)
                builder.addTarget(mPreviewSurface.getNativeSurface(mPreviewRequest.getPreviewSize()))
                mPreviewCaptureRequest = builder
            }
            previewSession.stopRepeating()
            previewSession.setRepeatingRequest(mPreviewCaptureRequest.build(), object : CameraCaptureSession.CaptureCallback() {
                internal var mOldAfState: Int? = null

                internal var mOldAeState: Int? = null

                override fun onCaptureCompleted(@NonNull session: CameraCaptureSession, @NonNull request: CaptureRequest, @NonNull result: TotalCaptureResult) {
                    val aeState = result.get(CaptureResult.CONTROL_AE_STATE)
                    val afState = result.get(CaptureResult.CONTROL_AF_STATE)

                    if (aeState != null) {
                        if (aeState != mOldAeState) {
                            CameraLog.hardware("Preview AE State Changed :: [%s] -> [%s]", "" + mOldAeState!!, aeState.toString())
                            mOldAeState = aeState
                        }
                    }
                    if (afState != null) {
                        if (afState != mOldAfState) {
                            CameraLog.hardware("Preview AF State Changed :: [%s] -> [%s]", "" + mOldAfState!!, afState.toString())
                            mOldAfState = afState
                        }
                    }
                }
            }, mControlHandler)

            mFlags = mFlags or FLAG_NOW_PREVIEW
        } catch (e: CameraAccessException) {
            throw CameraAccessFailedException(e)
        }

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun stopPreview() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun takePicture(env: CameraEnvironmentRequest?): PictureData {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {

        /**
         * プレビュー中である場合true
         */
        private val FLAG_NOW_PREVIEW = 0x01 shl 0

    }
}