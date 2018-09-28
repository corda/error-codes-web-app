package net.corda.tools.error.codes.server.commons.events

import net.corda.tools.error.codes.server.commons.identity.Entity
import net.corda.tools.error.codes.server.commons.identity.AbstractId
import net.corda.tools.error.codes.server.commons.identity.UuidGenerator
import java.time.Instant

abstract class AbstractEvent(id: EventId = EventId.newInstance()) : Entity<EventId>(id)

class EventId(value: String, timestamp: Instant) : AbstractId<String>(value, TYPE, timestamp) {

    companion object {
        private const val TYPE = "Event"

        @JvmStatic
        fun newInstance(value: String = UuidGenerator.next().toString(), timestamp: Instant = Instant.now()): EventId = EventId(value, timestamp)
    }
}