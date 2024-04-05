package com.skufbet.core.api.graphql.controller.event

import com.skufbet.core.api.event.service.EventService
import com.skufbet.core.api.graphql.model.event.Event
import com.skufbet.core.api.graphql.model.event.Tournament
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
class TournamentController(
    private val eventService: EventService
) {

    @SchemaMapping(typeName = "Event", field = "tournament")
    fun getTournament(event: Event) : Tournament {
        return eventService.findTournamentById(event.id)
    }
}
