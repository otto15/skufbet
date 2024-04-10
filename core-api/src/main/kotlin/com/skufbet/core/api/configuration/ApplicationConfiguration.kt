package com.skufbet.core.api.configuration

import com.skufbet.database.skufdb.configuration.SkufdbLiquibaseConfiguration
import com.skufbet.utils.database.id.PgSequenceIdGenerator
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.web.client.RestTemplate

@Configuration
@Import(
    SkufdbLiquibaseConfiguration::class,
)
class ApplicationConfiguration {
    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }

    @Bean
    fun betIdGenerator(@Qualifier("namedParameterJdbcTemplate") jdbcTemplate: NamedParameterJdbcTemplate) =
        PgSequenceIdGenerator(BET_ID_SEQ, jdbcTemplate)

    companion object {
        private const val BET_ID_SEQ = "bet_id_seq"
    }
}
