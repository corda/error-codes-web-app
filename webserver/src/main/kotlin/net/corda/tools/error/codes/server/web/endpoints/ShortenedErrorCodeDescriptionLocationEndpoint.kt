package net.corda.tools.error.codes.server.web.endpoints

import com.uchuhimo.konf.Config
import net.corda.tools.error.codes.server.application.annotations.Application
import net.corda.tools.error.codes.server.domain.*
import net.corda.tools.error.codes.server.web.endpoints.template.EndpointConfigProvider
import reactor.core.publisher.Mono
import javax.inject.Inject
import javax.inject.Named

@Named
internal class ShortenedErrorCodeDescriptionLocationEndpoint @Inject constructor(configuration: ShortenedErrorCodeDescriptionLocationEndpoint.Configuration, @Application private val locateDescription: (ErrorCode, ReleaseVersion, PlatformEdition, InvocationContext) -> Mono<ErrorDescriptionLocation>) : ErrorCodeDescriptionLocationEndpointTemplate(configuration, locateDescription) {

    interface Configuration : ErrorCodeDescriptionLocationEndpointTemplate.Configuration

    @Named
    internal class ErrorCodeConfigProvider @Inject constructor(applyConfigStandards: (Config) -> Config) : ShortenedErrorCodeDescriptionLocationEndpoint.Configuration, EndpointConfigProvider(CONFIGURATION_SECTION_PATH, applyConfigStandards) {

        private companion object {

            private const val CONFIGURATION_SECTION_PATH = "configuration.web.server.endpoints.error_code_short"
        }
    }
}