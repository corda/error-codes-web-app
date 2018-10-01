package net.corda.tools.error.codes.server.web

import net.corda.tools.error.codes.server.commons.events.EventStream
import net.corda.tools.error.codes.server.domain.loggerFor
import reactor.core.publisher.ofType
import reactor.core.scheduler.Schedulers
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Named

@Named
internal class LoggingEventsProcessor @Inject internal constructor(stream: EventStream) {

    private companion object {

        private val logger = loggerFor<LoggingEventsProcessor>()
    }

    init {
        with(stream.events.publishOn(Schedulers.elastic())) {
            ofType<WebServer.Event.Initialisation.Completed>().doOnNext(::logWebServerInitialisation).subscribe()
        }
    }

    private fun logWebServerInitialisation(event: WebServer.Event.Initialisation.Completed) {

        logger.info("Web server started listening on port '${event.port.value}' at ${LocalDateTime.ofInstant(event.createdAt, ZoneId.systemDefault())}. Event ID is '${event.id.value}'.")
    }
}