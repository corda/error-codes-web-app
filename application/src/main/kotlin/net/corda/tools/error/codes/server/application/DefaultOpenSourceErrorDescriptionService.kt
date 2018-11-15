package net.corda.tools.error.codes.server.application

import net.corda.tools.error.codes.server.application.annotations.Application
import net.corda.tools.error.codes.server.commons.events.EventSource
import net.corda.tools.error.codes.server.domain.*
import org.springframework.core.Ordered
import reactor.core.publisher.Mono
import java.net.URI
import javax.inject.Named

@Application
@Named
internal class DefaultOpenSourceErrorDescriptionService(override val source: EventSource<ErrorDescriptionService.Event>) : ErrorDescriptionService, Ordered {

    override fun getOrder(): Int = Ordered.HIGHEST_PRECEDENCE // + 2

    override fun close() {}

    override fun descriptionLocationFor(errorCode: ErrorCode, releaseVersion: ReleaseVersion, platformEdition: PlatformEdition, invocationContext: InvocationContext): Mono<ErrorDescriptionLocation> {

        return if (platformEdition == PlatformEdition.OpenSource) {
            Mono.just(ErrorDescriptionLocation.External(URI("https://www.stackoverflow.com/search?q=[corda]+errorCode+${errorCode.value}")))
        } else {
            Mono.empty()
        }
    }
}