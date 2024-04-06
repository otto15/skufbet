package com.skufbet.core.api.event.dao

import com.skufbet.core.api.graphql.model.content.Line
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class LineDao(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {
    fun getLinesByEventId(eventId: Int): List<Line> = jdbcTemplate.query(
        GET_LINES_BY_EVENT_ID,
        MapSqlParameterSource().addValue("event_id", eventId)
    ) { rs, _ -> Line(
        rs.getInt("id"),
        rs.getInt("event_id"),
        rs.getString("type"),
        rs.getObject("result_id", Int::class.javaObjectType),
        rs.getBoolean("is_closed")
    )}

    companion object {
        private val GET_LINES_BY_EVENT_ID = """
            SELECT id, event_id, type, result_id, is_closed
            from line
            where event_id = :event_id
        """.trimIndent()
    }
}
