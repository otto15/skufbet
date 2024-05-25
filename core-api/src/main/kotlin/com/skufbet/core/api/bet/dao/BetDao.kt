package com.skufbet.core.api.bet.dao

import com.skufbet.core.api.graphql.model.content.Bet
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class BetDao(
    @Qualifier("skufdbJdbcTemplate") private val jdbcTemplate: NamedParameterJdbcTemplate
) {
    fun create(bet: Bet) =
        jdbcTemplate.update(
            CREATE_BET,
            MapSqlParameterSource()
                .addValue("id", bet.id)
                .addValue("user_id", bet.userId)
                .addValue("line_id", bet.lineId)
                .addValue("result_id", bet.resultId)
                .addValue("amount", bet.amount)
                .addValue("coefficient", bet.coefficient)
                .addValue("status", bet.status)
        )

    fun changeStatus(betId: Int, status: String) =
        jdbcTemplate.update(
            CHANGE_STATUS,
            MapSqlParameterSource()
                .addValue("status", status)
                .addValue("id", betId)
        )

    fun getBy(betId: Int): Bet? =
        try {
            jdbcTemplate.queryForObject(
                GET_BY_ID,
                MapSqlParameterSource()
                    .addValue("id", betId)
            ) { rs, _ ->
                Bet(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getInt("line_id"),
                    rs.getInt("result_id"),
                    rs.getInt("amount"),
                    rs.getDouble("coefficient"),
                    rs.getString("status")
                )
            }
        } catch (e: EmptyResultDataAccessException) {
            null
        }

    fun selectBy(lineId: Int): List<Bet> =
        jdbcTemplate.query(
            """
                SELECT id, user_id, line_id, result_id, amount, coefficient, status
                FROM bet
                WHERE line_id = :line_id
            """.trimIndent(),
            MapSqlParameterSource().addValue("line_id", lineId)
        ) { rs, _ ->
            Bet(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getInt("line_id"),
                rs.getInt("result_id"),
                rs.getInt("amount"),
                rs.getDouble("coefficient"),
                rs.getString("status")
            )
        }

//    fun deleteFailed() =
//        jdbcTemplate.update(
//            DELETE_FAILED,
//            MapSqlParameterSource()
//                .addValue("status", status)
//                .addValue("id", betId)
//        )

    companion object {
        private val CREATE_BET = """
            INSERT INTO bet (id, user_id, line_id, result_id, amount, coefficient, status)
            VALUES (:id, :user_id, :line_id, :result_id, :amount, :coefficient, :status);
        """.trimIndent()
        private val CHANGE_STATUS = """
            UPDATE bet
            SET status = :status
            WHERE id = :id
        """.trimIndent()
        private val GET_BY_ID = """
            SELECT id, user_id, line_id, result_id, amount, coefficient, status
            FROM bet
            WHERE id = :id
        """.trimIndent()
        private val DELETE_FAILED = """
            DELETE FROM bet
            WHERE id AND status IN ('FAILED_BY_BALANCE', 'FAILED_BY_COEFFICIENT')
        """.trimIndent()
    }
}
