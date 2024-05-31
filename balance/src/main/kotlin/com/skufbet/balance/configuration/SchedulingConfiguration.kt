package com.skufbet.balance.configuration

import net.javacrumbs.shedlock.core.LockProvider
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import javax.sql.DataSource


@Configuration
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT30S")
class SchedulingConfiguration {
    @Bean
    @ConfigurationProperties("spring.datasource.scheduling")
    fun schedulingDataSourceProperties() = DataSourceProperties()

    @Bean
    fun schedulingDataSource(): DataSource = schedulingDataSourceProperties()
        .initializeDataSourceBuilder()
        .build()

    @Bean
    fun lockProvider(@Qualifier("schedulingDataSource") dataSource: DataSource): LockProvider {
        return JdbcTemplateLockProvider(dataSource)
    }
}