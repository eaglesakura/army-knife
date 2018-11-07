package com.eaglesakura.firearm.rpc.service

import android.os.Bundle
import com.eaglesakura.firearm.rpc.service.templates.RestfulProcedureRouter

/**
 * Server to client api.
 * Call from server.
 */
class ExampleProcedureClient {
    val router = RestfulProcedureRouter()

    /**
     * Ping to client.
     */
    val ping =
        router.procedure<ExampleProcedureServer.VoidBundle, ExampleProcedureServer.VoidBundle>("/ping") { proc ->
            proc.argumentsToBundle = { Bundle() }
            proc.bundleToArguments = { ExampleProcedureServer.VoidBundle() }
            proc.resultToBundle = { Bundle() }
            proc.bundleToResult = { ExampleProcedureServer.VoidBundle() }
        }
}