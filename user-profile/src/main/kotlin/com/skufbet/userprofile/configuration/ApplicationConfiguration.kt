package com.skufbet.userprofile.configuration

import com.skufbet.database.skufdb.configuration.SkufdbLiquibaseConfiguration
import com.skufbet.database.userprofile.UserProfileDbLiquibaseConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@Import(
    SkufdbLiquibaseConfiguration::class,
    UserProfileConfiguration::class,
    UserProfileDbLiquibaseConfiguration::class,
    SkufdbDatabaseConfiguration::class,
)
@EnableTransactionManagement
class ApplicationConfiguration
