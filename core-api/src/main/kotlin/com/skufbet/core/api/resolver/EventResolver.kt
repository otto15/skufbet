package com.skufbet.core.api.resolver

import com.skufbet.core.api.domain.Event
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.util.UUID

@Controller
class EventResolver {
    @QueryMapping("events")
    fun getEvents(): List<Event> {
        return listOf(Event(UUID.randomUUID(), "csgo"), Event(UUID.randomUUID(), "puk"))
    }
}
