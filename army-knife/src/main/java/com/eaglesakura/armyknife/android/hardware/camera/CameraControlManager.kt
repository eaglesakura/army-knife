package com.eaglesakura.armyknife.android.hardware.camera

import android.content.Context
import android.os.Build

import com.eaglesakura.armyknife.android.hardware.camera.error.CameraException
import com.eaglesakura.armyknife.android.hardware.camera.preview.CameraSurface

/**
 * 同期的なカメラ制御を提供する
 *
 *
 * 内部はAndroid 5.0以上であればCamera2 APIを、それ以外であればCamera1 APIを使用する
 *
 *
 * 互換性を保つため、撮影は必ずconnect > preview > takePicture の順で行わなければならない。
 * - 1. connect
 * - 2. startPreview
 * - 3. takePicture
 * - 4. stopPreview
 * - 5. disconnect
 */
abstract class CameraControlManager {
    /**
     * 利用しているAPIを取得する
     */
    abstract val supportApi: CameraApi

    /**
     * プレビュー中であればtrue
     */
    abstract val isPreviewNow: Boolean

    abstract val connected: Boolean

    /**
     * 撮影用の設定を指定して接続する
     *
     * @param previewSurface プレビュー用のサーフェイス
     * @param previewRequest プレビュー設定
     * @param shotRequest    撮影設定
     */
    @Throws(CameraException::class)
    abstract suspend fun connect(previewSurface: CameraSurface?, previewRequest: CameraPreviewRequest?, shotRequest: CameraPictureShotRequest?)

    abstract suspend fun disconnect()

    /**
     * カメラプレビューを開始する
     */
    @Throws(CameraException::class)
    abstract suspend fun startPreview(env: CameraEnvironmentRequest?)

    /**
     * カメラプレビューを停止する
     *
     *
     * MEMO: プレビューの停止はサーフェイスと同期して削除しなければならないため、実装的にはUIスレッド・バックグラウンドスレッドどちらでも動作できる。
     */
    @Throws(CameraException::class)
    abstract suspend fun stopPreview()

    /**
     * 写真撮影を行わせる
     */
    @Throws(CameraException::class)
    abstract suspend fun takePicture(env: CameraEnvironmentRequest?): PictureData

    companion object {

        /**
         * カメラ制御クラスを生成する
         */
        @Throws(CameraException::class)
        fun newInstance(context: Context, api: CameraApi, request: CameraConnectRequest): CameraControlManager {
            var api = api
            if (api == CameraApi.Default) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    api = CameraApi.Camera2
                } else {
                    api = CameraApi.Legacy
                }
            }

            TODO("impl")
            //        if (api == CameraApi.Camera2) {
            //            // Camera2
            //            return new Camera2ControlManager(context, request);
            //        } else {
            //            // Camera1
            //            return new LegacyCameraControlManager(context, request);
            //        }
        }

        /**
         * デフォルトのAPIでカメラ制御クラスを生成する
         */
        @Throws(CameraException::class)
        fun newInstance(context: Context, request: CameraConnectRequest): CameraControlManager {
            return newInstance(context, CameraApi.Default, request)
        }
    }
}
