package com.skufbet.database.balance.configuration

import liquibase.integration.spring.SpringLiquibase
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class BalanceDbLiquibaseConfiguration {
    @Bean
    fun liquibase(@Qualifier("balanceDataSource") dataSource: DataSource): SpringLiquibase {
        val liquibase = SpringLiquibase().apply {
            this.dataSource = dataSource
            changeLog = "classpath:/db/changelog/changelog.xml"
        }
        return liquibase
    }
}
