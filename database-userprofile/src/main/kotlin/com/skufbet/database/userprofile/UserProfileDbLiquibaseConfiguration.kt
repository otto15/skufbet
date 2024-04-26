package com.skufbet.database.userprofile

import liquibase.integration.spring.SpringLiquibase
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class UserProfileDbLiquibaseConfiguration {
    @Bean
    fun userProfileLiquibase(@Qualifier("userProfileDataSource") dataSource: DataSource): SpringLiquibase {
        val liquibase = SpringLiquibase().apply {
            this.dataSource = dataSource
            changeLog = "classpath:/userprofile/changelog/changelog.xml"
        }
        return liquibase
    }
}
