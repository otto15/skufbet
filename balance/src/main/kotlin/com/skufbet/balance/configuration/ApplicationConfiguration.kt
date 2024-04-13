package com.skufbet.balance.configuration

import com.skufbet.database.balance.configuration.BalanceDbLiquibaseConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.web.client.RestTemplate

@Configuration
@Import(
    BalanceDbLiquibaseConfiguration::class,
)
class ApplicationConfiguration {
    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }
}
