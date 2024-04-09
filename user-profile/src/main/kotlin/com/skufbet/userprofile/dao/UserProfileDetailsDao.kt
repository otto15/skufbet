package com.skufbet.core.api.userprofile.dao

import com.skufbet.core.api.userprofile.domain.UserProfileDetails
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class UserProfileDetailsDao(private val jdbcTemplate: NamedParameterJdbcTemplate) {
    fun insert(userProfileDetails: UserProfileDetails) {
        jdbcTemplate.update(
            INSERT_QUERY,
            MapSqlParameterSource()
                .addValue("user_profile_id", userProfileDetails.userProfileId)
                .addValue("first_name", userProfileDetails.firstName)
                .addValue("last_name", userProfileDetails.lastName)
                .addValue("passport", userProfileDetails.passport)
                .addValue("date_of_birth", userProfileDetails.dateOfBirth)
                .addValue("taxpayer_id", userProfileDetails.taxpayerId)
        )
    }

    companion object {
        private val INSERT_QUERY = """
            INSERT INTO user_profile_details (user_profile_id, first_name, last_name, passport, date_of_birth, taxpayer_id) 
            VALUES (:user_profile_id, :first_name, :last_name, :passport, :date_of_birth, :taxpayer_id);
        """.trimIndent()
    }
}
