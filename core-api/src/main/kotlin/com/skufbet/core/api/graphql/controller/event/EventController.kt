package com.skufbet.core.api.graphql.controller.event

import com.skufbet.core.api.graphql.model.event.Event
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class EventController {
    @QueryMapping("events")
    fun getEvents(): List<Event> {
        return listOf(
            Event(1, 0, 0, "csgo", false),
            Event(2, 1, 1, "dota 2", false)
        )
    }
}
