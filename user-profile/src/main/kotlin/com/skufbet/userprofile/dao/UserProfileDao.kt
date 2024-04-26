package com.skufbet.userprofile.dao

import com.skufbet.userprofile.domain.UserProfile
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
class UserProfileDao(
    @Qualifier("userProfileJdbcTemplate") private val jdbcTemplate: NamedParameterJdbcTemplate
) {
    fun insert(userProfile: UserProfile) {
        jdbcTemplate.update(
            INSERT_QUERY,
            MapSqlParameterSource()
                .addValue("id", userProfile.id)
                .addValue("mail", userProfile.mail)
                .addValue("phone_number", userProfile.phoneNumber)
                .addValue("password", userProfile.password)
                .addValue("balance", userProfile.balance)
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
                    rs.getString("mail"),
                    rs.getString("phone_number"),
                    rs.getString("password"),
                    rs.getInt("balance")
                )
            }
        } catch (e: EmptyResultDataAccessException) {
            null
        }

    fun getBy(id: Int): UserProfile? =
        try {
            jdbcTemplate.queryForObject(
                GET_BY_ID_QUERY,
                MapSqlParameterSource()
                    .addValue("id", id)
            ) { rs, _ ->
                UserProfile(
                    rs.getInt("id"),
                    rs.getString("mail"),
                    rs.getString("phone_number"),
                    rs.getString("password"),
                    rs.getInt("balance")
                )
            }
        } catch (e: Exception) {
            null
        }




    companion object {
        private val INSERT_QUERY = """
            INSERT INTO user_profile (id, mail, phone_number, password, balance) 
            VALUES (:id, :mail, :phone_number, :password, :balance)
        """.trimIndent()

        private val SELECT_BY_ID = """
            SELECT id, mail, phone_number, password, balance FROM user_profile
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
        private val GET_BY_ID_QUERY = """
            SELECT id, mail, phone_number, password, balance FROM user_profile
            WHERE id = :id
        """.trimIndent()
    }
}

