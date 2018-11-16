package net.corda.tools.error.codes.server.application

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.ConfigSpec
import net.corda.tools.error.codes.server.domain.ErrorCoordinates
import net.corda.tools.error.codes.server.domain.ErrorDescriptionLocation
import net.corda.tools.error.codes.server.domain.PlatformEdition
import reactor.core.publisher.Mono
import java.net.URI
import javax.inject.Inject
import javax.inject.Named

@Named(CachingErrorDescriptionService.redirectToSearchUrlQualifier)
internal class ConfigurableLocationDefaultValueResolver @Inject constructor(applyConfigStandards: (Config) -> Config) : (ErrorCoordinates) -> Mono<ErrorDescriptionLocation> {

    private companion object {

        private const val CONFIGURATION_SECTION_PATH = "configuration.application.service.messages.templates.lookup.miss"

        private object Spec : ConfigSpec(CONFIGURATION_SECTION_PATH) {

            val open_source by required<String>()
            val enterprise by required<String>()
        }
    }

    private val config = applyConfigStandards.invoke(Config { addSpec(Spec) })

    private val openSourceTemplate = config[Spec.open_source]
    private val enterpriseTemplate = config[Spec.enterprise]

    override fun invoke(coordinates: ErrorCoordinates): Mono<ErrorDescriptionLocation> {

        return Mono.just(ErrorDescriptionLocation.External(URI(String.format(coordinates.template(), coordinates.code.value))))
    }

    private fun ErrorCoordinates.template() = if (platformEdition == PlatformEdition.OpenSource) openSourceTemplate else enterpriseTemplate
}