package com.skufbet.core.api.configuration

import com.skufbet.core.api.auth.AuthInstrumentation
import com.skufbet.core.api.client.userprofile.UserProfileApiClient
import com.skufbet.core.api.scheduling.configuration.SchedulingConfiguration
import com.skufbet.database.skufdb.configuration.SkufdbLiquibaseConfiguration
import com.skufbet.utils.database.id.PgSequenceIdGenerator
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.web.client.RestTemplate
import java.util.concurrent.ScheduledThreadPoolExecutor
import javax.sql.DataSource

@Configuration
@Import(
    SkufdbLiquibaseConfiguration::class,
    SchedulingConfiguration::class,
    KafkaProducerConfiguration::class,
)
class ApplicationConfiguration {
    @Bean
    fun authInstrumentation(userProfileApiClient: UserProfileApiClient) = AuthInstrumentation(userProfileApiClient)

    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }

    @Bean
    fun betIdGenerator(@Qualifier("skufdbJdbcTemplate") jdbcTemplate: NamedParameterJdbcTemplate) =
        PgSequenceIdGenerator(BET_ID_SEQ, jdbcTemplate)

    @Bean
    fun coefficientValidatingScheduledPool() = ScheduledThreadPoolExecutor(10)

    @Bean
    @ConfigurationProperties("spring.datasource.skufdb")
    fun skufdbDataSourceProperties() = DataSourceProperties()

    @Primary
    @Bean
    fun skufdbDataSource(): DataSource = skufdbDataSourceProperties()
        .initializeDataSourceBuilder()
        .build()

    @Bean
    fun skufdbJdbcTemplate(@Qualifier("skufdbDataSource") dataSource: DataSource) =
        NamedParameterJdbcTemplate(dataSource)

    companion object {
        private const val BET_ID_SEQ = "bet_id_seq"
    }
}
