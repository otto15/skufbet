package com.skufbet.core.api.userprofile.dao

import com.skufbet.core.api.userprofile.domain.UserProfile
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class UserProfileDao(val jdbcTemplate: NamedParameterJdbcTemplate) {
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

    companion object {
        private val INSERT_QUERY = """
            INSERT INTO user_profile (id, mail, phone_number, password, balance) 
            VALUES (:id, :mail, :phone_number, :password, :balance);
        """.trimIndent()
    }
}
