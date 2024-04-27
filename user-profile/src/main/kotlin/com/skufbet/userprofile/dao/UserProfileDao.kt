package com.skufbet.userprofile.dao

import com.skufbet.common.userprofile.domain.UserProfile
import com.skufbet.common.userprofile.domain.UserProfileRole
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.sql.Connection
import javax.sql.DataSource

class UserProfileDao(
    @Qualifier("userProfileJdbcTemplate") private val jdbcTemplate: NamedParameterJdbcTemplate,
) {
    fun insert(userProfile: UserProfile) {
        jdbcTemplate.update(
            INSERT_QUERY,
            MapSqlParameterSource()
                .addValue("id", userProfile.id)
                .addValue("keycloak_id", userProfile.keycloakId)
                .addValue("mail", userProfile.mail)
                .addValue("phone_number", userProfile.phoneNumber)
                .addValue("password", userProfile.password)
                .addValue("balance", userProfile.balance)
                .addValue("role", userProfile.role.name)
        )
    }

    fun updateBalanceMinus(id: Int, amount: Int): Boolean =
        jdbcTemplate.update(
            UPDATE_BALANCE_MINUS,
            MapSqlParameterSource()
                .addValue("id", id)
                .addValue("amount", amount)
        ) > 0


    fun updateBalancePlus(id: Int, amount: Int): Boolean =
        jdbcTemplate.update(
            UPDATE_BALANCE_PLUS,
            MapSqlParameterSource()
                .addValue("id", id)
                .addValue("amount", amount)
        ) > 0

    fun selectBy(id: Int): UserProfile? =
        try {
            jdbcTemplate.queryForObject(
                SELECT_BY_ID,
                MapSqlParameterSource()
                    .addValue("id", id)
            ) { rs, _ ->
                UserProfile(
                    rs.getInt("id"),
                    rs.getString("keycloak_id"),
                    rs.getString("mail"),
                    rs.getString("phone_number"),
                    rs.getString("password"),
                    rs.getInt("balance"),
                    UserProfileRole.valueOf(rs.getString("role"))
                )
            }
        } catch (e: EmptyResultDataAccessException) {
            null
        }

    fun selectByKeycloakId(keycloakId: String): UserProfile? =
        try {
            jdbcTemplate.queryForObject(
                SELECT_BY_KEYCLOAK_ID,
                MapSqlParameterSource()
                    .addValue("keycloak_id", keycloakId)
            ) { rs, _ ->
                UserProfile(
                    rs.getInt("id"),
                    rs.getString("keycloak_id"),
                    rs.getString("mail"),
                    rs.getString("phone_number"),
                    rs.getString("password"),
                    rs.getInt("balance"),
                    UserProfileRole.valueOf(rs.getString("role"))
                )
            }
        } catch (e: Exception) {
            null
        }




    companion object {
        private val INSERT_QUERY = """
            INSERT INTO user_profile (id, keycloak_id, mail, phone_number, password, balance, role) 
            VALUES (:id, :keycloak_id, :mail, :phone_number, :password, :balance, :role)
        """.trimIndent()

        private val SELECT_BY_ID = """
            SELECT id, keycloak_id, mail, phone_number, password, balance, role FROM user_profile
            WHERE id = :id
        """.trimIndent()

        private val UPDATE_BALANCE_MINUS = """
            UPDATE user_profile 
            SET balance = balance - :amount
            WHERE id = :id AND balance >= :amount
        """.trimIndent()

        private val UPDATE_BALANCE_PLUS = """
            UPDATE user_profile 
            SET balance = balance + :amount
            WHERE id = :id
        """.trimIndent()
        private val SELECT_BY_KEYCLOAK_ID = """
            SELECT id, keycloak_id, mail, phone_number, password, balance, role FROM user_profile
            WHERE keycloak_id = :keycloak_id
        """.trimIndent()
    }
}

