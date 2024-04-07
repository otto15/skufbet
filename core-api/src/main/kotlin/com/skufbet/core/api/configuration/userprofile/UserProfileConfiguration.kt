package com.skufbet.core.api.configuration.userprofile

import com.skufbet.core.api.userprofile.dao.UserProfileDao
import com.skufbet.core.api.userprofile.dao.UserProfileDetailsDao
import com.skufbet.core.api.userprofile.service.UserProfileCreationService
import com.skufbet.skufdb.id.IdGenerator
import com.skufbet.skufdb.id.PgSequenceIdGenerator
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class UserProfileConfiguration {
    @Bean
    fun userProfileDao(@Qualifier("namedParameterJdbcTemplate") jdbcTemplate: NamedParameterJdbcTemplate) =
        UserProfileDao(jdbcTemplate)

    @Bean
    fun userProfileDetailsDao(@Qualifier("namedParameterJdbcTemplate") jdbcTemplate: NamedParameterJdbcTemplate) =
        UserProfileDetailsDao(jdbcTemplate)

    @Bean
    fun userProfileCreationService(
        @Qualifier("userProfileIdGenerator") idGenerator: IdGenerator<Int>,
        @Qualifier("userProfileDao") userProfileDao: UserProfileDao,
        @Qualifier("userProfileDetailsDao") userProfileDetailsDao: UserProfileDetailsDao,
        @Qualifier("encoder") passwordEncoder: PasswordEncoder,
    ) = UserProfileCreationService(idGenerator, userProfileDao, userProfileDetailsDao, passwordEncoder)

    @Bean
    fun userProfileIdGenerator(@Qualifier("namedParameterJdbcTemplate") jdbcTemplate: NamedParameterJdbcTemplate) =
        PgSequenceIdGenerator(USER_PROFILE_ID_SEQ, jdbcTemplate)

    companion object {
        private const val USER_PROFILE_ID_SEQ = "user_profile_id_seq"
    }
}
