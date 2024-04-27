package com.skufbet.userprofile.configuration

import com.atomikos.jdbc.AtomikosDataSourceBean
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
class UserProfileDatabaseConfiguration {
    @Bean
    fun userProfileAtomikosDataSource(
        @Value("\${spring.datasource.userprofile.xa.properties.user}") user: String,
        @Value("\${spring.datasource.userprofile.xa.properties.password}") password: String,
        @Value("\${spring.datasource.userprofile.xa.properties.url}") url: String,
        @Value("\${spring.datasource.userprofile.xa.data-source-class-name}") dscn: String
    ) = AtomikosDataSourceBean().also {
        it.uniqueResourceName = "userprofile"
        it.xaDataSourceClassName = dscn
        it.xaProperties["user"] = user
        it.xaProperties["password"] = password
        it.xaProperties["url"] = url
        it.maxPoolSize = 100
    }

    @Bean
    fun userProfileJdbcTemplate(@Qualifier("userProfileAtomikosDataSource") userProfileDataSource: DataSource) =
        NamedParameterJdbcTemplate(userProfileDataSource)
}
