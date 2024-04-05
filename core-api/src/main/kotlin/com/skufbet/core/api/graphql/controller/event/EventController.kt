package com.skufbet.core.api.graphql.controller.event

import com.skufbet.core.api.event.service.EventService
import com.skufbet.core.api.graphql.model.content.Event
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class EventController(
    private val eventService: EventService
) {
    @QueryMapping("events")
    fun getEvents(): List<Event> {
        return eventService.findAll()
    }
}
