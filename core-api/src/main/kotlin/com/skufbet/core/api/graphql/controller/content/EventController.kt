package com.skufbet.core.api.graphql.controller.content

import com.skufbet.core.api.content.service.EventService
import com.skufbet.core.api.graphql.model.content.Event
import com.skufbet.core.api.kafka.MessageProducer
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class EventController(
    private val eventService: EventService,

    private val messageProducer: MessageProducer,
) {
    @QueryMapping("events")
    fun getEvents(): List<Event> {
         messageProducer.sendMessage("skufbet", "Aloha")

        return eventService.findAll()
    }
}
