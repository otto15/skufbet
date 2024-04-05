package com.skufbet.core.api.configuration

import com.skufbet.core.api.configuration.userprofile.UserProfileConfiguration
import com.skufbet.skufdb.configuration.SkufdbLiquibaseConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
@Import(
    SkufdbLiquibaseConfiguration::class,
    UserProfileConfiguration::class,
)
class ApplicationConfiguration {
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}
