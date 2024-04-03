package com.skufbet.core.api.configuration

import com.skufbet.skufdb.configuration.SkufdbLiquibaseConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(SkufdbLiquibaseConfiguration::class)
class ApplicationConfiguration {
}
