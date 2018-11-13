package net.corda.tools.error.codes.server.web.endpoints

import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.http.HttpMethod
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.Router
import net.corda.tools.error.codes.server.commons.domain.ValidationResult
import net.corda.tools.error.codes.server.commons.domain.ValidationResult.Companion.invalid
import net.corda.tools.error.codes.server.commons.domain.ValidationResult.Companion.valid
import net.corda.tools.error.codes.server.domain.*
import net.corda.tools.error.codes.server.web.endpoints.template.ConfigurableEndpoint
import reactor.core.publisher.Mono

internal abstract class ErrorCodeDescriptionLocationEndpointTemplate(configuration: ErrorCodeDescriptionLocationEndpointTemplate.Configuration, private val locateDescription: (ErrorCode, ReleaseVersion, PlatformEdition, InvocationContext) -> Mono<ErrorDescriptionLocation>) : ConfigurableEndpoint(configuration, setOf(HttpMethod.GET)) {

    private companion object {

        private const val PLATFORM_EDITION = "platform_edition"
        private const val RELEASE_VERSION = "release_version"
        private const val ERROR_CODE = "error_code"
    }

    override fun install(router: Router) {

        serve(router.get(path)) { context ->

            withPathParam(PLATFORM_EDITION, ::platformEdition) { platformEdition ->
                withPathParam(RELEASE_VERSION, ::releaseVersion) { releaseVersion ->
                    withPathParam(ERROR_CODE, ErrorCode.Valid::create) { errorCode ->

                        locateDescription(errorCode, releaseVersion, platformEdition, context).thenIfPresent(this) { location -> response().end(location) }
                    }
                }
            }
        }
    }

    private fun HttpServerResponse.end(location: ErrorDescriptionLocation) {

        when (location) {
            is ErrorDescriptionLocation.External -> location.writeTo(this).end()
        }
    }

    private fun ErrorDescriptionLocation.External.writeTo(response: HttpServerResponse): HttpServerResponse {

        return response.putHeader(HttpHeaderNames.LOCATION, uri.toASCIIString()).setStatusCode(HttpResponseStatus.TEMPORARY_REDIRECT.code())
    }

    private fun platformEdition(rawValue: String): ValidationResult<PlatformEdition> {

        return when (rawValue) {
            "OS" -> ValidationResult.valid(PlatformEdition.OpenSource)
            "ENT" -> ValidationResult.valid(PlatformEdition.Enterprise)
            else -> invalid(setOf("Invalid platform edition path parameter. Valid values are [\"OS\", \"ENT\"]."))
        }
    }

    interface Configuration : ConfigurableEndpoint.Configuration
}

private val INVALID_VERSION = invalid<ReleaseVersion>(setOf("Invalid release version path parameter format. Use <major>.<minor>[.<patch>][-SNAPSHOT] e.g., \"3.2\" or \"4.0.2-SNAPSHOT\"."))

internal fun releaseVersion(rawValue: String): ValidationResult<ReleaseVersion> {
    val rawParts = rawValue.split(ReleaseVersion.SEPARATOR).map { it.split("-").let { parts -> parts[0] to parts.getOrNull(1) } }
    return when {
        rawParts.size < 2 || rawParts.size > 3 -> INVALID_VERSION
        rawParts.any { it.second != "SNAPSHOT" && it.second != null } -> INVALID_VERSION
        rawParts[0].second != null -> INVALID_VERSION
        rawParts[1].second != null -> if (rawParts.size == 3) {
            INVALID_VERSION
        } else {
            try {
                valid(ReleaseVersion(rawParts[0].first.toInt(), rawParts[1].first.toInt(), snapshot = rawParts[1].second != null))
            } catch (e: Exception) {
                INVALID_VERSION
            }
        }
        else -> try {
            valid(ReleaseVersion(rawParts[0].first.toInt(), rawParts[1].first.toInt(), rawParts[2].first.toInt(), snapshot = rawParts[2].second != null))
        } catch (e: Exception) {
            INVALID_VERSION
        }
    }
}