package com.eaglesakura.firearm.property

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import com.eaglesakura.armyknife.property.TextDatabasePropertyStore

class PropertyProviderHandler(context: Context) : UriHandler {
    private val propertyStore: TextDatabasePropertyStore =
        TextDatabasePropertyStore(context, "com.eaglesakura.props_v5.db")

    override fun query(uri: Uri, command: String, arguments: Array<String>): ByteArray? {
        if (COMMAND_GET == command) {
            val propKey = arguments[0]
            val value = propertyStore.getStringProperty(propKey)
            return if (value.isEmpty()) {
                ByteArray(0)
            } else {
                value.toByteArray()
            }
        }
        return ByteArray(0)
    }

    override fun insert(uri: Uri, command: String, values: ContentValues) {
        when (command) {
            COMMAND_SET -> {
                val propKey = values.getAsString("propKey")
                val value = values.getAsString("value")

                // 全てデータが揃ったらput
                if (!propKey.isEmpty()) {
                    propertyStore.setProperty(propKey, value)
                }
            }
            COMMAND_CLEAR -> {
                // データをクリアする
                propertyStore.clear()
            }
            COMMAND_COMMIT -> {
                // データをコミットする
                propertyStore.commit()
            }
            COMMAND_LOAD -> {
                propertyStore.load()
            }
        }
    }

    companion object {
        const val COMMAND_GET = "prop.GET"

        const val COMMAND_SET = "prop.SET"

        const val COMMAND_COMMIT = "prop.COMMIT"

        const val COMMAND_CLEAR = "prop.COMMAND_CLEAR"

        const val COMMAND_LOAD = "prop.LOAD"
    }
}
