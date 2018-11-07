package com.eaglesakura.firearm.rpc.service

import android.os.Bundle
import com.eaglesakura.armyknife.runtime.Random
import com.eaglesakura.firearm.aidl.IRemoteProcedureClient

/**
 * Client interface in Server process.
 */
class RemoteClient internal constructor(
    private val parent: ProcedureServiceBinder,
    internal val aidl: IRemoteProcedureClient
) {
    /**
     * Unique id of Client.
     */
    val id: String = Random.smallString()

    /**
     * Request to client(from server).
     * run client task.
     */
    suspend fun request(path: String, arguments: Bundle): Bundle {
        return parent.request(this, path, arguments)
    }
}