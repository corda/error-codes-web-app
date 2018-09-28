package net.corda.tools.error.codes.server.commons.vertx.web

import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.Router

interface Endpoint {

    fun install(router: Router)

    val path: String
    val name: String
    val enabled: Boolean
    val methods: Set<HttpMethod>
}