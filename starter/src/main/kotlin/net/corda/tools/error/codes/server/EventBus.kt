package net.corda.tools.error.codes.server

import net.corda.tools.error.codes.server.commons.events.AbstractEvent
import net.corda.tools.error.codes.server.commons.events.EventSource
import net.corda.tools.error.codes.server.commons.events.MultiplexingEventStream
import net.corda.tools.error.codes.server.domain.loggerFor
import javax.annotation.PreDestroy
import javax.inject.Inject
import javax.inject.Named

@Named
class EventBus @Inject constructor(sources: List<EventSource<AbstractEvent>>) : MultiplexingEventStream(sources) {

    private companion object {

        private val logger = loggerFor<EventBus>()
    }

    @PreDestroy
    override fun close() {

        super.close()
        logger.info("Closed")
    }
}