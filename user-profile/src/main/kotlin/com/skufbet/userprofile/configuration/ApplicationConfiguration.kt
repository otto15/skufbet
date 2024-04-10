package com.skufbet.userprofile.configuration

import com.skufbet.database.skufdb.configuration.SkufdbLiquibaseConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
    SkufdbLiquibaseConfiguration::class,
    UserProfileConfiguration::class,
)
class ApplicationConfiguration