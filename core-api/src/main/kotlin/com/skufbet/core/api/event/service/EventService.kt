package com.skufbet.core.api.event.service

<<<<<<< HEAD
import com.skufbet.core.api.event.dao.EventDao
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
=======
class EventService {
}
>>>>>>> e792c87 (add graph controllers for event and tournament)
