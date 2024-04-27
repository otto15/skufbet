package com.skufbet.userprofile.configuration

import com.skufbet.database.skufdb.configuration.SkufdbLiquibaseConfiguration
import com.skufbet.database.userprofile.UserProfileDbLiquibaseConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@Import(
    SkufdbLiquibaseConfiguration::class,
    UserProfileConfiguration::class,
    UserProfileDbLiquibaseConfiguration::class,
    SkufdbDatabaseConfiguration::class,
)
@EnableTransactionManagement
class ApplicationConfiguration
class ApplicationConfiguration {
    @Bean
    fun internalUnregisteredUserMethodArgumentResolver() = InternalUnregisteredUserMethodArgumentResolver()

    @Bean
    fun internalUserIdentityWebMvcConfigurer(
        internalUnregisteredUserMethodArgumentResolver: InternalUnregisteredUserMethodArgumentResolver
    ): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
                if (!resolvers.contains(internalUnregisteredUserMethodArgumentResolver)) {
                    resolvers.add(internalUnregisteredUserMethodArgumentResolver)
                }
            }
        }
    }
}
