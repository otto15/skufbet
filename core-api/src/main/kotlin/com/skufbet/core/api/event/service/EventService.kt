package com.skufbet.core.api.event.service

import com.skufbet.core.api.event.dao.EventDao
import com.skufbet.core.api.graphql.model.event.Event
import com.skufbet.core.api.graphql.model.event.Tournament
import org.springframework.stereotype.Service

@Service
class EventService(
    private val eventDao: EventDao
) {

    fun findAll() : List<Event> = eventDao.findAll()

    fun findTournamentById(id: Int): Tournament = eventDao.findTournamentById(id)
}
