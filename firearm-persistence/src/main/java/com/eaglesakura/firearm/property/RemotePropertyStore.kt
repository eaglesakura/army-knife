package com.eaglesakura.firearm.property

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import com.eaglesakura.armyknife.property.PropertyStore

/**
 * ContentProviderと通信してプロパティを管理する
 *
 * Content Provider example.
 * <provider android:authorities="${applicationId}.internal.properties.provider"
 *         android:name="replace.your.Provider"
 *         android:exported="false"/>
 */
class RemotePropertyStore(private val context: Context, uri: String = "") : PropertyStore {

    private val propertyUri: Uri

    init {
        if (uri.isEmpty()) {
            this.propertyUri = Uri.parse("content://${context.packageName}.internal.properties.provider")
        } else {
            this.propertyUri = Uri.parse("content://$uri")
        }
    }

    override fun getStringProperty(key: String): String {
        val buffer = PropertiesProviderDelegate.query(context, propertyUri, PropertyProviderHandler.COMMAND_GET, arrayOf(key))
        return if (buffer != null) {
            String(buffer)
        } else {
            throw IllegalStateException("Key[$key] not found in [$propertyUri]")
        }
    }

    override fun setProperty(key: String, value: String) {
        val values = ContentValues()
        values.put("propKey", key)
        values.put("value", value)

        PropertiesProviderDelegate.insert(context, propertyUri, PropertyProviderHandler.COMMAND_SET, values)
    }

    override fun clear() {
        val values = ContentValues()
        PropertiesProviderDelegate.insert(context, propertyUri, PropertyProviderHandler.COMMAND_CLEAR, values)
    }

    override fun commit() {
        val values = ContentValues()
        PropertiesProviderDelegate.insert(context, propertyUri, PropertyProviderHandler.COMMAND_COMMIT, values)
    }

    override fun load() {
        val values = ContentValues()
        PropertiesProviderDelegate.insert(context, propertyUri, PropertyProviderHandler.COMMAND_LOAD, values)
    }
}
