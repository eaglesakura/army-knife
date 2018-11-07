package com.eaglesakura.firearm.rpc.service.routers

import android.os.Bundle
import com.eaglesakura.firearm.rpc.service.ProcedureServiceConnection
import com.eaglesakura.firearm.rpc.service.RemoteClient
import kotlinx.coroutines.CancellationException

class RestfulClientProcedure<Arguments, ProcedureResult>(
    /**
     * Procedure path.
     */
    val path: String
) {
    /**
     * Convert Arguments to Bundle.
     */
    lateinit var argumentsToBundle: (arguments: Arguments) -> Bundle

    /**
     * Convert Bundle to Arguments.
     */
    lateinit var bundleToArguments: (arguments: Bundle) -> Arguments

    /**
     * Convert Bundle to ProcedureResult.
     */
    lateinit var bundleToResult: (result: Bundle) -> ProcedureResult

    /**
     * Convert ProcedureResult to Bundle.
     */
    lateinit var resultToBundle: (result: ProcedureResult) -> Bundle

    /**
     * Convert exception.
     */
    var errorMap: (error: Exception) -> Exception = { it }

    /**
     * Implementation stub for Client.
     */
    internal lateinit var clientProcedure: suspend (connection: ProcedureServiceConnection, arguments: Bundle) -> Bundle
        private set

    /**
     * Request handler in client.
     */
    fun listenInClient(block: suspend (connection: ProcedureServiceConnection, arguments: Arguments) -> ProcedureResult) {
        clientProcedure = { connection, arg ->
            try {
                resultToBundle(block(connection, bundleToArguments(arg)))
            } catch (err: CancellationException) {
                throw err
            } catch (err: Exception) {
                throw errorMap(err)
            }
        }
    }

    /**
     * Request server to client.
     * Execute in client.
     */
    suspend operator fun invoke(
        client: RemoteClient,
        arguments: Arguments
    ): ProcedureResult {
        try {
            return bundleToResult(client.request(path, argumentsToBundle(arguments)))
        } catch (err: CancellationException) {
            throw err
        } catch (err: Exception) {
            throw errorMap(err)
        }
    }
}