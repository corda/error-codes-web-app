package net.corda.tools.error.codes.server.application

import net.corda.tools.error.codes.server.application.annotations.Application
import net.corda.tools.error.codes.server.commons.events.EventSource
import net.corda.tools.error.codes.server.domain.*
import org.springframework.core.Ordered
import reactor.core.publisher.Mono
import reactor.core.publisher.Mono.empty
import java.net.URI
import javax.inject.Named

@Application
@Named
internal class DefaultEnterpriseErrorDescriptionService(override val source: EventSource<ErrorDescriptionService.Event>) : ErrorDescriptionService, Ordered {

    override fun getOrder(): Int = Ordered.HIGHEST_PRECEDENCE + 1

    override fun close() {}

    override fun descriptionLocationFor(errorCode: ErrorCode, releaseVersion: ReleaseVersion, platformEdition: PlatformEdition, invocationContext: InvocationContext): Mono<ErrorDescriptionLocation> {

        return if (platformEdition == PlatformEdition.Enterprise) {
            Mono.just(ErrorDescriptionLocation.External(URI("https://support.r3.com/")))
        } else {
            empty()
        }
    }
}

