package com.skufbet.userprofile.configuration

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import javax.sql.DataSource

@Configuration
class SkufdbDatabaseConfiguration {
    @Bean
    @ConfigurationProperties("spring.datasource.skufdb")
    fun skufdbDataSourceProperties() = DataSourceProperties()

    @Bean
    fun skufdbDataSource() = skufdbDataSourceProperties()
        .initializeDataSourceBuilder()
        .build()

    @Bean
    fun skufdbJdbcTemplate(@Qualifier("skufdbDataSource") skufdbDataSource: DataSource) =
        NamedParameterJdbcTemplate(skufdbDataSource)
}
