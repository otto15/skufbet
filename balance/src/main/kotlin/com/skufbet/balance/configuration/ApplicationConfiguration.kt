package com.skufbet.balance.configuration

import com.skufbet.database.balance.configuration.BalanceDbLiquibaseConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
    BalanceDbLiquibaseConfiguration::class,
)
class ApplicationConfiguration
