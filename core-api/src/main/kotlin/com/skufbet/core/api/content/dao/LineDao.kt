package com.skufbet.core.api.content.dao

import com.skufbet.core.api.graphql.model.content.Line
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class LineDao(
    @Qualifier("skufdbJdbcTemplate") private val jdbcTemplate: NamedParameterJdbcTemplate
) {
    fun getLinesByEventId(eventId: Int): List<Line> = jdbcTemplate.query(
        GET_LINES_BY_EVENT_ID,
        MapSqlParameterSource().addValue("event_id", eventId)
    ) { rs, _ ->
        Line(
            rs.getInt("id"),
            rs.getInt("event_id"),
            rs.getString("type"),
            rs.getObject("result_id", Int::class.javaObjectType),
            rs.getBoolean("is_closed")
        )
    }

    fun selectBy(id: Int): Line? =
        try {
            jdbcTemplate.queryForObject(
                """
                SELECT id, event_id, type, result_id, is_closed
                FROM line
                WHERE id = :id 
                """.trimIndent(),
                MapSqlParameterSource().addValue("id", id)
            ) { rs, _ ->
                Line(
                    rs.getInt("id"),
                    rs.getInt("event_id"),
                    rs.getString("type"),
                    rs.getObject("result_id", Int::class.javaObjectType),
                    rs.getBoolean("is_closed")
                )
            }
        } catch (e: EmptyResultDataAccessException) {
            null
        }

    fun updateResult(id: Int, resultId: Int) {
        jdbcTemplate.update(
            """
                UPDATE line
                SET result_id = :result_id
                WHERE id = :id
            """.trimIndent(),
            MapSqlParameterSource()
                .addValue("id", id)
                .addValue("result_id", resultId)
        )
    }

    companion object {
        private val GET_LINES_BY_EVENT_ID = """
            SELECT id, event_id, type, result_id, is_closed
            from line
            where event_id = :event_id
        """.trimIndent()
    }
}
