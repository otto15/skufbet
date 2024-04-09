package com.skufbet.core.api.configuration

import com.skufbet.skufdb.configuration.SkufdbLiquibaseConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.web.client.RestTemplate

@Configuration
@Import(
    SkufdbLiquibaseConfiguration::class,
)
class ApplicationConfiguration {
    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }

}
