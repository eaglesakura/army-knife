package com.eaglesakura.armyknife.android.extensions

import android.os.Parcel
import android.os.Parcelable
import kotlin.reflect.KClass

/**
 * Copy object with new instance by Parcel.
 */
fun <T : Parcelable> Parcelable.deepCopy(): T {
    val data = marshal()
    @Suppress("UNCHECKED_CAST")
    return this.javaClass.kotlin.unmarshal(data) as T
}

/**
 * Convert Parcelable to ByteArray.
 */
fun Parcelable.marshal(): ByteArray {
    val parcel = Parcel.obtain()
    try {
        writeToParcel(parcel, 0)
        return parcel.marshall()
    } finally {
        parcel.recycle()
    }
}

/**
 * Convert ByteArray to Parcelable
 */
fun <T : Parcelable> Parcelable.Creator<T>.unmarshal(data: ByteArray): T {
    val parcel = Parcel.obtain()
    try {
        parcel.unmarshall(data, 0, data.size)
        parcel.setDataPosition(0)
        return createFromParcel(parcel) as T
    } finally {
        parcel.recycle()
    }
}

/**
 * Convert ByteArray to Parcelable with Reflection.
 *
 * If you used Kotlin-Android-Extensions(@Parcel annotation) in class, then can not access "CREATOR" field.
 * So, this function access field by reflection.
 * It speed is very slowly.
 */
inline fun <reified T : Parcelable> KClass<T>.unmarshal(data: ByteArray): T {
    val javaClass = java
    @Suppress("UNCHECKED_CAST")
    val creator = javaClass.getField("CREATOR").get(javaClass) as Parcelable.Creator<T>
    return creator.unmarshal(data)
}