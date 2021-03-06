package net.corda.tools.error.codes.server.application

import net.corda.tools.error.codes.server.commons.events.EventStream
import net.corda.tools.error.codes.server.domain.ErrorDescriptionLocation
import net.corda.tools.error.codes.server.domain.loggerFor
import reactor.core.publisher.ofType
import reactor.core.scheduler.Schedulers
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Named

@Named
internal class ApplicationLoggingEventsProcessor @Inject internal constructor(stream: EventStream) {
    private companion object {
        private val logger = loggerFor<ApplicationLoggingEventsProcessor>()
    }

    init {
        with(stream.events.publishOn(Schedulers.elastic())) {
            ofType<ErrorDescriptionService.Event.Invocation.Completed.DescriptionLocationFor.WithDescriptionLocation>().doOnNext(::recordRequestedErrorCode).subscribe()
            ofType<ErrorDescriptionService.Event.Invocation.Completed.DescriptionLocationFor.WithoutDescriptionLocation>().filter(::isForAStableRelease).doOnNext(::warnAboutUnmappedErrorCode).subscribe()
        }
    }

    private fun recordRequestedErrorCode(event: ErrorDescriptionService.Event.Invocation.Completed.DescriptionLocationFor.WithDescriptionLocation) {
        val locationUri = (event.location as ErrorDescriptionLocation.External).uri.toASCIIString()
        logger.info(event.invocationContext, "Description requested for error code '${event.errorCoordinates.code.value}' at ${LocalDateTime.ofInstant(event.createdAt, ZoneId.systemDefault())}, link was '$locationUri'. Event ID: '${event.id.value}'.")
    }

    private fun warnAboutUnmappedErrorCode(event: ErrorDescriptionService.Event.Invocation.Completed.DescriptionLocationFor.WithoutDescriptionLocation) {
        // Here we could let ourselves know that an error code has no description, so that we can then go and add it to the mappings.
        logger.warn(event.invocationContext, "No description location known for error code '${event.errorCoordinates.code.value}' at ${LocalDateTime.ofInstant(event.createdAt, ZoneId.systemDefault())}. Event ID: '${event.id.value}'.")
    }

    private fun isForAStableRelease(event: ErrorDescriptionService.Event.Invocation.Completed.DescriptionLocationFor) = !event.errorCoordinates.releaseVersion.snapshot
}