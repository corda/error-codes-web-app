package net.corda.tools.error.codes.server.commons.vertx.web.test

import io.vertx.ext.web.client.HttpRequest
import io.vertx.ext.web.client.HttpResponse
import reactor.core.publisher.Mono
import reactor.core.publisher.MonoProcessor

fun <RESULT> HttpRequest<RESULT>.asyncResponse(): Mono<HttpResponse<RESULT>> {

    val promise = MonoProcessor.create<HttpResponse<RESULT>>()
    send { call ->

        if (call.succeeded()) {
            promise.onNext(call.result())
        } else {
            promise.onError(call.cause())
        }
    }
    return promise
}