package com.skufbet.balance.dao

import com.skufbet.balance.domain.BalanceOperation
import com.skufbet.balance.domain.BalanceOperationType
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class BalanceOperationDao(private val jdbcTemplate: NamedParameterJdbcTemplate) {
    fun insert(balanceOperation: BalanceOperation) {
        jdbcTemplate.update(
            """
            INSERT INTO balance_operation (id, user_profile_id, amount, type, status)
            VALUES (:id, :user_profile_id, :amount, :type, :status)
            """.trimIndent(),
            MapSqlParameterSource()
                .addValue("id", balanceOperation.id)
                .addValue("user_profile_id", balanceOperation.userProfileId)
                .addValue("amount", balanceOperation.amount)
                .addValue("type", balanceOperation.type.name)
                .addValue("status", balanceOperation.status.name)
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
                    SELECT id, payment_token, user_profile_id, amount, type, status
                    FROM balance_operation 
                    WHERE payment_token = :payment_token
                """.trimIndent(),
                MapSqlParameterSource().addValue("payment_token", paymentToken)
            ) { rs, _ ->
                BalanceOperation(
                    rs.getObject("id", UUID::class.javaObjectType),
                    rs.getString("payment_token"),
                    rs.getInt("user_profile_id"),
                    rs.getInt("amount"),
                    BalanceOperationType.valueOf(rs.getString("type")),
                    BalanceOperation.Status.valueOf(rs.getString("status"))
                )
            }
        } catch (e: EmptyResultDataAccessException) {
            null
        }
}
