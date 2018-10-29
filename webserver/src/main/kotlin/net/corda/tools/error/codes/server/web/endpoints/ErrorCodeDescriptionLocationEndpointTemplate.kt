package net.corda.tools.error.codes.server.web.endpoints

import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.http.HttpMethod
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.Router
import net.corda.tools.error.codes.server.commons.domain.ValidationResult
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

    private fun releaseVersion(rawValue: String): ValidationResult<ReleaseVersion> {

        val rawParts = rawValue.split(ReleaseVersion.SEPARATOR)
        if (rawParts.size < 2 || rawParts.size > 3) {
            return ValidationResult.invalid(setOf("Invalid release version path parameter format. Use <major>.<minor>[.<patch>] e.g., \"3.2\" or \"4.0.2\"."))
        }
        val major = rawParts[0].toIntOrNull()
                ?: return ValidationResult.invalid(setOf("Major release version part should be a non-negative integer."))
        val minor = rawParts[1].toIntOrNull()
                ?: return ValidationResult.invalid(setOf("Minor release version part should be a non-negative integer."))
        val patch = (if (rawParts.size == 3) rawParts[2] else "0").toIntOrNull()
                ?: return ValidationResult.invalid(setOf("Patch release version part should be a non-negative integer."))

        return ReleaseVersion.Valid.create(major, minor, patch)
    }

    private fun platformEdition(rawValue: String): ValidationResult<PlatformEdition> {

        return when (rawValue) {
            "OS" -> ValidationResult.valid(PlatformEdition.OpenSource)
            "ENT" -> ValidationResult.valid(PlatformEdition.Enterprise)
            else -> ValidationResult.invalid(setOf("Invalid platform edition path parameter. Valid values are [\"OS\", \"ENT\"]."))
        }
    }

    interface Configuration : ConfigurableEndpoint.Configuration
}