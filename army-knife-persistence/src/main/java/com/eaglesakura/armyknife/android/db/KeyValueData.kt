package com.eaglesakura.armyknife.android.db

import java.util.Date

/**
 * Key-Value row in TextKeyValueStore.
 * It is simple, lite, and fast.
 */
class KeyValueData(val key: String, val value: String, val date: Date)