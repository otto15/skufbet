package com.skufbet.skufdb.configuration

import liquibase.integration.spring.SpringLiquibase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class SkufdbLiquibaseConfiguration {
    @Bean
    fun liquibase(dataSource: DataSource): SpringLiquibase {
        val liquibase = SpringLiquibase().apply {
            this.dataSource = dataSource
            changeLog = "classpath:/db/changelog/changelog.xml"
        }
        return liquibase
    }

}
