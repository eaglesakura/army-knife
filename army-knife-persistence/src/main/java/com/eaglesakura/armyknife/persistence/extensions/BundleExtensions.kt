@file:Suppress("unused")

package com.eaglesakura.armyknife.persistence.extensions

import android.os.Bundle
import android.os.Parcelable
import com.eaglesakura.armyknife.android.os.*

/**
 * Bundle to kotlin delegate.
 *
 * e.g.)
 * val foo: Int by bundle.delegateIntExtra("EXTRA_FOO", 0)
 *
 * fun bar() {
 *      foo = 3 // bundle.putExtra("EXTRA_FOO", 3)
 * }
 */
fun Bundle.delegateIntExtra(key: String, defValue: Int): BundleExtra<Int> =
    BundleIntExtra(this, key, defValue)

/**
 * Bundle to kotlin delegate.
 *
 * e.g.)
 * val foo: Int by bundle.delegateIntExtra("EXTRA_FOO", 0)
 *
 * fun bar() {
 *      foo = 3 // bundle.putExtra("EXTRA_FOO", 3)
 * }
 */
fun Bundle.delegateLongExtra(key: String, defValue: Long): BundleExtra<Long> =
    BundleLongExtra(this, key, defValue)

/**
 * Bundle to kotlin delegate.
 *
 * e.g.)
 * val foo: Int by bundle.delegateIntExtra("EXTRA_FOO", 0)
 *
 * fun bar() {
 *      foo = 3 // bundle.putExtra("EXTRA_FOO", 3)
 * }
 */
fun Bundle.delegateFloatExtra(key: String, defValue: Float): BundleExtra<Float> =
    BundleFloatExtra(this, key, defValue)

/**
 * Bundle to kotlin delegate.
 *
 * e.g.)
 * val foo: Int by bundle.delegateIntExtra("EXTRA_FOO", 0)
 *
 * fun bar() {
 *      foo = 3 // bundle.putExtra("EXTRA_FOO", 3)
 * }
 */
fun Bundle.delegateDoubleExtra(key: String, defValue: Double): BundleExtra<Double> =
    BundleDoubleExtra(this, key, defValue)

/**
 * Bundle to kotlin delegate.
 *
 * e.g.)
 * val foo: Int by bundle.delegateIntExtra("EXTRA_FOO", 0)
 *
 * fun bar() {
 *      foo = 3 // bundle.putExtra("EXTRA_FOO", 3)
 * }
 */
fun Bundle.delegateStringExtra(key: String, defValue: String): BundleExtra<String> =
    BundleStringExtra(this, key, defValue)

/**
 * Bundle to kotlin delegate.
 *
 * e.g.)
 * val foo: Int by bundle.delegateIntExtra("EXTRA_FOO", 0)
 *
 * fun bar() {
 *      foo = 3 // bundle.putExtra("EXTRA_FOO", 3)
 * }
 */
fun Bundle.delegateByteArrayExtra(key: String): BundleExtra<ByteArray?> =
    BundleByteArrayExtra(this, key)

/**
 * Bundle to kotlin delegate.
 *
 * e.g.)
 * val foo: Int by bundle.delegateIntExtra("EXTRA_FOO", 0)
 *
 * fun bar() {
 *      foo = 3 // bundle.putExtra("EXTRA_FOO", 3)
 * }
 */
fun <T : Parcelable> Bundle.delegateParcelableExtra(
    key: String
): BundleExtra<T?> = BundleParcelableExtra(this, key)

/**
 * Bundle to kotlin delegate.
 *
 * e.g.)
 * val foo: Int by bundle.delegateIntExtra("EXTRA_FOO", 0)
 *
 * fun bar() {
 *      foo = 3 // bundle.putExtra("EXTRA_FOO", 3)
 * }
 */
fun Bundle.delegateBundleExtra(
    key: String
): BundleExtra<Bundle?> = BundleInBundleExtra(this, key)
