package com.skufbet.core.api.content.service

import com.skufbet.core.api.content.dao.EventDao
import com.skufbet.core.api.graphql.model.content.Event
import com.skufbet.core.api.graphql.model.content.Tournament
import org.springframework.stereotype.Service

@Service
class EventService(
    private val eventDao: EventDao
) {
    fun findAll() : List<Event> = eventDao.findAll()

    fun findTournamentById(id: Int): Tournament? = eventDao.findTournamentById(id)
}
