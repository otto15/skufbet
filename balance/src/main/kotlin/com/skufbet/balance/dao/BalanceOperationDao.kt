package com.skufbet.balance.dao

import com.skufbet.balance.domain.BalanceOperation
import com.skufbet.balance.domain.BalanceOperationType
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
class BalanceOperationDao(private val jdbcTemplate: NamedParameterJdbcTemplate) {
    fun insert(balanceOperation: BalanceOperation) {
        jdbcTemplate.update(
            """
            INSERT INTO balance_operation (id, user_profile_id, amount, type, status, created_date)
            VALUES (:id, :user_profile_id, :amount, :type, :status, :created_date)
            """.trimIndent(),
            MapSqlParameterSource()
                .addValue("id", balanceOperation.id)
                .addValue("user_profile_id", balanceOperation.userProfileId)
                .addValue("amount", balanceOperation.amount)
                .addValue("type", balanceOperation.type.name)
                .addValue("status", balanceOperation.status.name)
                .addValue("created_date", Date.from(balanceOperation.createdDate))
        )
    }

    fun updatePaymentTokenAndStatus(balanceOperation: BalanceOperation) {
        jdbcTemplate.update(
            """
            UPDATE balance_operation
            SET payment_token = :payment_token, status = :status
            WHERE id = :id
            """.trimIndent(),
            MapSqlParameterSource()
                .addValue("id", balanceOperation.id)
                .addValue("payment_token", balanceOperation.paymentToken)
                .addValue("status", balanceOperation.status.name)
        )
    }

    fun updateStatus(balanceOperation: BalanceOperation) {
        jdbcTemplate.update(
            """
            UPDATE balance_operation
            SET status = :status
            WHERE id = :id
            """.trimIndent(),
            MapSqlParameterSource()
                .addValue("id", balanceOperation.id)
                .addValue("status", balanceOperation.status.name)
        )
    }

    fun selectBy(paymentToken: String): BalanceOperation? =
        try {
            jdbcTemplate.queryForObject(
                """
                    SELECT id, payment_token, user_profile_id, amount, type, status, created_date
                    FROM balance_operation 
                    WHERE payment_token = :payment_token
                """.trimIndent(),
                MapSqlParameterSource().addValue("payment_token", paymentToken),
                MAPPER
            )
        } catch (e: EmptyResultDataAccessException) {
            null
        }

    fun selectBy(status: BalanceOperation.Status, endExclusiveDate: Instant): List<BalanceOperation> =
        jdbcTemplate.query(
            """
                SELECT id, payment_token, user_profile_id, amount, type, status, created_date
                FROM balance_operation 
                WHERE status = :status and created_date < :date
            """.trimIndent(),
            MapSqlParameterSource().addValue("status", status.name)
                .addValue("date", Date.from(endExclusiveDate)),
            MAPPER
        )

    fun selectForUpdateBy(id: UUID): BalanceOperation? =
        try {
            jdbcTemplate.queryForObject(
                """
                    SELECT id, payment_token, user_profile_id, amount, type, status, created_date
                    FROM balance_operation 
                    WHERE id = :id
                """.trimIndent(),
                MapSqlParameterSource().addValue("id", id),
                MAPPER
            )
        } catch (e: EmptyResultDataAccessException) {
            null
        }

    companion object {
        private val MAPPER: RowMapper<BalanceOperation> = RowMapper { rs, _ ->
            BalanceOperation(
                rs.getObject("id", UUID::class.javaObjectType),
                rs.getString("payment_token"),
                rs.getInt("user_profile_id"),
                rs.getInt("amount"),
                BalanceOperationType.valueOf(rs.getString("type")),
                BalanceOperation.Status.valueOf(rs.getString("status")),
                rs.getTimestamp("created_date").toInstant()
            )
        }
    }
}
