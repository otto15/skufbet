package com.skufbet.userprofile.configuration

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import javax.sql.DataSource

@Configuration
class UserProfileDatabaseConfiguration {
    @Bean
    @ConfigurationProperties("spring.datasource.userprofile")
    fun userProfileDataSourceProperties() = DataSourceProperties()

    @Bean
    fun userProfileDataSource() = userProfileDataSourceProperties()
        .initializeDataSourceBuilder()
        .build()

    @Bean
    fun userProfileJdbcTemplate(@Qualifier("userProfileDataSource") userProfileDataSource: DataSource) =
        NamedParameterJdbcTemplate(userProfileDataSource)
}
