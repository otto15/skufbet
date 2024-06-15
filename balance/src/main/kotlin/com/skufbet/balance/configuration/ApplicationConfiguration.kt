package com.skufbet.balance.configuration

import com.skufbet.database.balance.configuration.BalanceDbLiquibaseConfiguration
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.web.client.RestTemplate
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
@Import(
    BalanceDbLiquibaseConfiguration::class,
    SchedulingConfiguration::class,
)
class ApplicationConfiguration {
    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }

    @Bean
    @ConfigurationProperties("spring.datasource.balance")
    fun balanceDataSourceProperties() = DataSourceProperties()

    @Bean
    fun balanceDataSource(): DataSource = balanceDataSourceProperties()
        .initializeDataSourceBuilder()
        .build()

    @Bean
    fun balanceJdbcTemplate(@Qualifier("balanceDataSource") balanceDataSource: DataSource) =
        NamedParameterJdbcTemplate(balanceDataSource)

    @Bean("balanceTransactionManager")
    fun balanceTransactionManager(@Qualifier("balanceDataSource") balanceDataSource: DataSource) =
        DataSourceTransactionManager(balanceDataSource)
}
