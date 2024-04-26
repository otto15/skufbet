package com.skufbet.database.skufdb.configuration

import liquibase.integration.spring.SpringLiquibase
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class SkufdbLiquibaseConfiguration {
    @Bean
    fun skufdbLiquibase(@Qualifier("skufdbDataSource") dataSource: DataSource): SpringLiquibase {
        val liquibase = SpringLiquibase().apply {
            this.dataSource = dataSource
            changeLog = "classpath:/skufdb/changelog/changelog.xml"
        }
        return liquibase
    }
}
