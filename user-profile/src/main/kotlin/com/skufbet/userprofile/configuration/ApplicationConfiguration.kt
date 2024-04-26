package com.skufbet.userprofile.configuration

import com.skufbet.database.skufdb.configuration.SkufdbLiquibaseConfiguration
import com.skufbet.database.userprofile.UserProfileDbLiquibaseConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
    SkufdbLiquibaseConfiguration::class,
    UserProfileConfiguration::class,
    UserProfileDbLiquibaseConfiguration::class,
    SkufdbDatabaseConfiguration::class,
)
class ApplicationConfiguration
