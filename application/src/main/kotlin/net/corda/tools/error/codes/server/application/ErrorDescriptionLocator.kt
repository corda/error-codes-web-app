package net.corda.tools.error.codes.server.application

import net.corda.tools.error.codes.server.application.annotations.Application
import net.corda.tools.error.codes.server.domain.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import javax.inject.Inject
import javax.inject.Named

@Application
@Named
internal class ErrorDescriptionLocator @Inject constructor(
        private val services: List<ErrorDescriptionService>
) : (ErrorCode, ReleaseVersion, PlatformEdition, InvocationContext) -> Mono<ErrorDescriptionLocation> {

    companion object {

        private val logger = loggerFor<ErrorDescriptionLocator>()
    }

    override fun invoke(errorCode: ErrorCode, releaseVersion: ReleaseVersion, platformEdition: PlatformEdition, invocationContext: InvocationContext): Mono<ErrorDescriptionLocation> {

        // Take the first error location handler that returns a result
        return Flux.fromIterable(services)
                .flatMap { it.descriptionLocationFor(errorCode, releaseVersion, platformEdition, invocationContext) }
                .takeUntil { it != null }
                .filter { it != null }
                .singleOrEmpty()
    }
}