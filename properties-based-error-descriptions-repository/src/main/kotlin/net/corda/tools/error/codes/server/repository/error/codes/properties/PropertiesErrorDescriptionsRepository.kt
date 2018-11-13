package net.corda.tools.error.codes.server.repository.error.codes.properties

import net.corda.tools.error.codes.server.commons.lifecycle.Startable
import net.corda.tools.error.codes.server.domain.*
import net.corda.tools.error.codes.server.domain.annotations.Adapter
import net.corda.tools.error.codes.server.domain.repository.descriptions.ErrorDescriptionsRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Flux.empty
import reactor.core.publisher.toFlux
import java.net.URI
import java.util.*
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Named

@Adapter
@Named
internal class PropertiesErrorDescriptionsRepository @Inject constructor(@Adapter private val loadProperties: () -> Properties) : ErrorDescriptionsRepository, Startable {

    private companion object {

        private const val DESCRIPTIONS_SEPARATOR = "|"
        private const val DESCRIPTION_PARTS_SEPARATOR = ","
    }

    private var map = mapOf<ErrorCode, List<ErrorDescription>>()

    @PostConstruct
    override fun start() {

        map = loadProperties.invoke().entries.map { entry -> ErrorCode(entry.key as String) to (entry.value as String).split(DESCRIPTIONS_SEPARATOR).filter(String::isNotBlank).map { parseDescription(it, ErrorCode(entry.key as String)) } }.toMap()
    }

    override operator fun get(errorCode: ErrorCode, invocationContext: InvocationContext): Flux<out ErrorDescription> {

        return map[errorCode]?.toFlux() ?: empty()
    }

    private fun parseDescription(rawValue: String, errorCode: ErrorCode): ErrorDescription {

        // Here we can trust the file format to be correct.
        val parts = rawValue.split(DESCRIPTION_PARTS_SEPARATOR)
        val location = ErrorDescriptionLocation.External(URI.create(parts[0]))
        val platformEdition = parts[1].let { if (it == "OS") PlatformEdition.OpenSource else PlatformEdition.Enterprise }
        val releaseVersion = parts[2].split(ReleaseVersion.SEPARATOR).map(String::toInt).let { releaseNumbers -> ReleaseVersion(releaseNumbers[0], releaseNumbers[1], releaseNumbers.getOrNull(2) ?: 0) }
        val coordinates = ErrorCoordinates(errorCode, releaseVersion, platformEdition)
        return ErrorDescription(location, coordinates)
    }
}

// This allows injecting functions instead of types.
@Adapter
@Named
internal class RetrieveErrorDescription @Inject constructor(@Adapter private val service: ErrorDescriptionsRepository) : (ErrorCode, InvocationContext) -> Flux<out ErrorDescription> {

    override fun invoke(errorCode: ErrorCode, invocationContext: InvocationContext) = service[errorCode, invocationContext]
}
