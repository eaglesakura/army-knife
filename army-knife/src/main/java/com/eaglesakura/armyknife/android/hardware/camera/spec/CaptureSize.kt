package com.eaglesakura.armyknife.android.hardware.camera.spec

/**
 * 撮影・プレビュー用のサイズを返す
 */
class CaptureSize(width: Int, height: Int) {
    private val mSize: Array<Int> = arrayOf(width, height)

    /**
     * アスペクト比のIDを取得する
     */
    val aspectType: Aspect?

    /**
     * ピクセル数をメガピクセル単位で取得する
     *
     * @return 計算されたメガピクセル
     */
    val megaPixel: Double
        get() = (width * height).toDouble() / 1000.0 / 1000.0

    val width: Int
        get() = mSize[0]

    val height: Int
        get() = mSize[1]

    /**
     * ユーザー表示用のメガピクセル数を取得する。
     * <br></br>
     * 小数点第一位まで計算する
     * <br></br>
     * 例) 5.0
     * <br></br>
     * 例)13.1
     *
     * @return 表示用のメガピクセル
     */
    val megaPixelText: String
        get() = String.format("%.1f", megaPixel)

    /**
     * アスペクト比表示用テキストを取得する
     * 例) 16:9
     */
    val aspectText: String
        get() = aspectType!!.aspectText()

    /**
     * アスペクト比(width / height)を取得する
     */
    val aspect: Double
        get() = width.toDouble() / height.toDouble()

    /**
     * 一意に識別するためのIDを取得する
     */
    val id: String
        get() = String.format("pic(%dx%d)", width, height)

    enum class Aspect {
        /**
         * 縦横1:1
         */
        WH1x1 {
            override fun aspect(): Double {
                return 1.0
            }

            override fun aspectText(): String {
                return "1:1"
            }
        },

        /**
         * 縦横3x2
         */
        WH3x2 {
            override fun aspect(): Double {
                return 3.0 / 2.0
            }

            override fun aspectText(): String {
                return "3:2"
            }
        },
        /**
         * 縦横4:3
         */
        WH4x3 {
            override fun aspect(): Double {
                return 4.0 / 3.0
            }

            override fun aspectText(): String {
                return "4:3"
            }
        },

        /**
         * 縦横16:9
         */
        WH16x9 {
            override fun aspect(): Double {
                return 16.0 / 9.0
            }

            override fun aspectText(): String {
                return "16:9"
            }
        },

        /**
         * 縦横16:10
         */
        WH16x10 {
            override fun aspect(): Double {
                return 16.0 / 10.0
            }

            override fun aspectText(): String {
                return "16:10"
            }
        };

        /**
         * 横ピクセル数 / 縦ピクセル数のアスペクト比を取得する
         */
        abstract fun aspect(): Double

        /**
         * アスペクト比のテキストを取得する
         * <br></br>
         * 例：16:9
         */
        abstract fun aspectText(): String

        companion object {

            /**
             * 最も近いアスペクト比を取得する
             */
            internal fun getNearAspect(aspect: Double): Aspect? {
                var diffNear = 99999999.0
                var result: Aspect? = null

                val values = values()
                for (value in values) {
                    val checkDiff = Math.abs(value.aspect() - aspect)
                    if (checkDiff < diffNear) {
                        // 差が小さいならコレにする
                        result = value
                        // 次はコレが比較対象
                        diffNear = checkDiff
                    }
                }
                return result
            }
        }
    }

    init {
        this.aspectType = Aspect.getNearAspect(aspect)
    }

    /**
     * CaptureSizeの縦横比を満たし、かつminWidth/minHeight以上の大きさを返却する
     *
     * @param flipOrientation 縦横サイズを入れ替えている場合はtrue
     * @param minWidth        最小限の幅
     * @param minHeight       最小限の高さ
     * @return 新しい縦横サイズ
     */
    fun getViewSize(flipOrientation: Boolean, minWidth: Int, minHeight: Int): Array<Int> {

        var aspect = aspect.toFloat()
        if (flipOrientation) {
            aspect = height.toFloat() / width.toFloat()
        }
        val result = arrayOf(minWidth, minHeight)
        result[0] = (minHeight * aspect).toInt()
        if (result[0] < minWidth) {
            result[0] = minWidth
            result[1] = (minWidth / aspect).toInt()
        }
        return result
    }
}
