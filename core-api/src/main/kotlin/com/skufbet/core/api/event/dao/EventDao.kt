package com.skufbet.core.api.event.dao

import com.skufbet.core.api.graphql.model.event.Event
import com.skufbet.core.api.graphql.model.event.Tournament
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class EventDao(val jdbcTemplate: NamedParameterJdbcTemplate) {

    fun findAll() : List<Event> = jdbcTemplate.query(FIND_ALL) {
        rs, _ -> Event(
            rs.getInt("id"),
            rs.getInt("tournament_id"),
            rs.getInt("sport_id"),
            rs.getString("event_name"),
            rs.getBoolean("is_end")
        )
    }

    fun findTournamentById(id: Int) : Tournament = jdbcTemplate.queryForObject(
        FIND_TOURNAMENT_BY_ID,
        MapSqlParameterSource().addValue("tournament_id", id)
    ) { rs, _ -> Tournament(
        rs.getInt("id"),
        rs.getString("tournament_name"),
        rs.getBoolean("is_end")
    )
    }!!

    companion object {
        private val FIND_ALL = """
            SELECT id, tournament_id, sport_id, event_name, is_end
            from event
        """.trimIndent()
        private val FIND_TOURNAMENT_BY_ID = """
            SELECT id, tournament_name, is_end
            from tournament
            where id = :tournament_id
        """.trimIndent()
    }
}
