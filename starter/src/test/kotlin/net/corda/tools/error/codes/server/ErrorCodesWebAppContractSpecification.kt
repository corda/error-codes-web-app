package net.corda.tools.error.codes.server

import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.ext.web.client.HttpResponse
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.WebClientOptions
import net.corda.tools.error.codes.server.commons.vertx.web.test.asyncResponse
import net.corda.tools.error.codes.server.domain.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.net.URI

interface ErrorCodesWebAppContractSpecification {

    @Test
    fun found_location_is_returned_as_temporary_redirect() {

        val errorCoordinates = ErrorCoordinates(ErrorCode("123jdazz"), ReleaseVersion(4, 3, 1), PlatformEdition.OpenSource)

        val location = ErrorDescriptionLocation.External(URI.create("https://thisisatest/boom"))
        val description = ErrorDescription(location, errorCoordinates)

        val response = performRequestWithStubbedValue(errorCoordinates, Flux.just(description)).block()!!

        assertThat(response.statusCode()).isEqualTo(HttpResponseStatus.TEMPORARY_REDIRECT.code())
        assertThat(response.headers()[HttpHeaderNames.LOCATION]).isEqualTo(location.uri.toASCIIString())
    }

    @Test
    fun absent_location_results_in_not_found() {

        val errorCoordinates = ErrorCoordinates(ErrorCode("123jdazz"), ReleaseVersion(4, 3, 1), PlatformEdition.Enterprise)

        val response = performRequestWithStubbedValue(errorCoordinates, Flux.empty()).block()!!

        assertThat(response.statusCode()).isEqualTo(HttpResponseStatus.NOT_FOUND.code())
        assertThat(response.headers()[HttpHeaderNames.LOCATION]).isNull()
    }

    fun startWebServerWithStubbedRepository(errorCoordinatesForServer: ErrorCoordinates, descriptionsReturned: Flux<out ErrorDescription>): Int

    private fun performRequestWithStubbedValue(errorCoordinatesForServer: ErrorCoordinates, descriptionsReturned: Flux<out ErrorDescription>): Mono<HttpResponse<Buffer>> {

        val webServerPort = startWebServerWithStubbedRepository(errorCoordinatesForServer, descriptionsReturned)

        val vertx = Vertx.vertx()
        val client = webClient(webServerPort, vertx)

        return client.get(path(errorCoordinatesForServer)).followRedirects(false).asyncResponse().doAfterTerminate(client::close).doAfterTerminate(vertx::close)
    }

    // This should stay hard-coded, rather than read from the actual configuration, to avoid breaking the contract without breaking the test.
    private fun path(coordinates: ErrorCoordinates): String = "/editions/${coordinates.platformEdition.description}/releases/${coordinates.releaseVersion.description()}/errors/${coordinates.code.value}"

    private fun webClient(port: Int, vertx: Vertx): WebClient = WebClient.create(vertx, WebClientOptions().setDefaultHost("localhost").setDefaultPort(port))
}