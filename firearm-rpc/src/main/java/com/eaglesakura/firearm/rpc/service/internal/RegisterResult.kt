package com.eaglesakura.firearm.rpc.service.internal

import android.os.Bundle
import com.eaglesakura.armyknife.persistence.extensions.delegateBundleExtra
import com.eaglesakura.armyknife.persistence.extensions.delegateStringExtra
import com.eaglesakura.armyknife.runtime.Random

/**
 * Result at IRemoteProcedureServer.register()
 *
 * @see IRemoteProcedureServerImpl
 */
internal class RegisterResult internal constructor(
    internal val bundle: Bundle = Bundle()
) {
    /**
     * Unique id of your client.
     */
    var clientId: String by bundle.delegateStringExtra("request.EXTRA_ID", Random.smallString())

    /**
     * Options in register.
     */
    var connectionHings: Bundle? by bundle.delegateBundleExtra("request.EXTRA_OPTIONS")
}