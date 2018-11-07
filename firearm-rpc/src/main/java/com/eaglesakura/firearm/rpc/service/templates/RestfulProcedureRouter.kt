package com.eaglesakura.firearm.rpc.service.templates

import android.os.Bundle
import com.eaglesakura.firearm.rpc.service.ProcedureServiceConnection
import com.eaglesakura.firearm.rpc.service.RemoteClient

class RestfulProcedureRouter {
    private val table = mutableMapOf<String, RestfulProcedure<*, *>>()

    fun <Arguments, ProcedureResult> procedure(
        path: String, builder: (procedure: RestfulProcedure<Arguments, ProcedureResult>) -> Unit
    ):
            RestfulProcedure<Arguments, ProcedureResult> {
        return RestfulProcedure<Arguments, ProcedureResult>(path).also { proc ->
            builder(proc)
            require(proc.argumentsToBundle == proc.argumentsToBundle)
            require(proc.bundleToArguments == proc.bundleToArguments)
            require(proc.resultToBundle == proc.resultToBundle)
            require(proc.bundleToResult == proc.bundleToResult)
            table[path] = proc
        }
    }

    /**
     * Handler in server.
     */
    suspend fun requestFrom(
        client: RemoteClient,
        path: String,
        arguments: Bundle
    ): Bundle {
        val proc = table[path] ?: throw IllegalArgumentException("Path[$path] not match")
        return proc.serverProcedure(client, arguments)
    }

    /**
     * Handler in client.
     */
    suspend fun requestFrom(
        connection: ProcedureServiceConnection,
        path: String,
        arguments: Bundle
    ): Bundle {
        val proc = table[path] ?: throw IllegalArgumentException("Path[$path] not match")
        return proc.clientProcedure(connection, arguments)
    }
}