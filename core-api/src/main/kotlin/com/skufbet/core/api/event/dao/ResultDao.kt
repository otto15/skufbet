package com.skufbet.core.api.event.dao

import com.skufbet.core.api.graphql.model.content.Result
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class ResultDao(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {
    fun getResultById(resultId: Int): Result? = jdbcTemplate.queryForObject(
        GET_RESULT_BY_ID,
        MapSqlParameterSource().addValue("id", resultId)
    ) { rs, _ ->
        Result(
            rs.getInt("id"),
            rs.getInt("line_id"),
            rs.getString("result"),
            rs.getDouble("coefficient")
        )
    }

    fun getAvailableResultsForLine(lineIds: List<Int>): List<Result> = jdbcTemplate.query(
        GET_AVAILABLE_RESULTS_FOR_LINE,
        MapSqlParameterSource().addValue("ids", lineIds)
    ) { rs, _ ->
        Result(
            rs.getInt("id"),
            rs.getInt("line_id"),
            rs.getString("result"),
            rs.getDouble("coefficient")
        )
    }

    companion object {
        private val GET_RESULT_BY_ID = """
            SELECT id, line_id, result, coefficient
            from line_result
            where id = :id
        """.trimIndent()
        private val GET_AVAILABLE_RESULTS_FOR_LINE = """
            SELECT id, line_id, result, coefficient
            from line_result
            where line_id in (:ids)
        """.trimIndent()
    }
}
