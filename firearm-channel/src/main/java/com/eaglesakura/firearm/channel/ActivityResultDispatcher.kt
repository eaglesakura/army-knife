package com.eaglesakura.firearm.channel

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.CheckResult
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

/**
 * Activity Dispatcher with Channel.
 *
 * e.g.)
 *
 * // in coroutines.
 * val dispatcher = ActivityResultDispatcher(fragment, registry)
 * val activityResult = dispatcher.startActivityForResultWithWait( /* arguments... */ )
 */
class ActivityResultDispatcher internal constructor(
    private val getActivity: () -> Activity,
    private val startActivityForResultCall: (intent: Intent, requestCode: Int, options: Bundle?) -> Unit,
    private val registry: ChannelRegistry
) {

    /**
     * Initialize from fragment.
     */
    constructor(fragment: Fragment, registry: ChannelRegistry) : this(
        getActivity = { fragment.activity!! },
        startActivityForResultCall = { intent, requestCode, options ->
            fragment.activity!!.startActivityFromFragment(fragment, intent, requestCode, options)
        },
        registry = registry
    )

    private fun makeKey(requestCode: Int) = "activity@$requestCode"

    private fun makeRequestCode(intent: Intent) = intent.hashCode() and 0x0000FFFF

    /**
     * start activity, with await.
     * You shouldn't keep channel, and "close()" channel.
     */
    suspend fun startActivityForResultWithResult(
        intent: Intent,
        requestCode: Int = makeRequestCode(intent),
        options: Bundle? = null
    ): ActivityResult {
        return startActivityForResult(intent, requestCode, options).use {
            it.receive()
        }
    }

    /**
     * start activity.
     *
     * You should check result channel, and close().
     * Call this function from coroutines? can replace to "requestPermissionsWithResult()" function.
     */
    @CheckResult
    @UiThread
    fun startActivityForResult(
        intent: Intent,
        requestCode: Int = makeRequestCode(intent),
        options: Bundle? = null
    ): Channel<ActivityResult> {
        assertUIThread()

        // check permissions
        val key = makeKey(requestCode)
        startActivityForResultCall(intent, requestCode and 0x0000FFFF, options)
        return registry.register(key, Channel())
    }

    @UiThread
    fun onActivityResult(requestCode: Int, result: Int, data: Intent?) {
        assertUIThread()
        val key = makeKey(requestCode)
        GlobalScope.launch(Dispatchers.Main) {
            registry.find<ActivityResult>(key)?.send(
                ActivityResult(
                    requestCode = requestCode,
                    result = result,
                    data = data
                )
            )
        }
    }
}