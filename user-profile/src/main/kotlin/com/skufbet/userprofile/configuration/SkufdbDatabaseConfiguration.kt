package com.skufbet.userprofile.configuration

import com.atomikos.jdbc.AtomikosDataSourceBean
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
class SkufdbDatabaseConfiguration {
    @Bean
    @ConfigurationProperties("spring.datasource.skufdb")
    fun skufdbDataSourceProperties() = DataSourceProperties()

    @Bean
    fun skufdbDataSource() = skufdbDataSourceProperties()
        .initializeDataSourceBuilder()
        .build()

    @Bean
    fun skufdbAtomikosDataSource(
        @Value("\${spring.datasource.skufdb.xa.properties.user}") user: String,
        @Value("\${spring.datasource.skufdb.xa.properties.password}") password: String,
        @Value("\${spring.datasource.skufdb.xa.properties.url}") url: String,
        @Value("\${spring.datasource.skufdb.xa.data-source-class-name}") dscn: String
    ) = AtomikosDataSourceBean().also {
        it.uniqueResourceName = "skufdb"
        it.xaDataSourceClassName = dscn
        it.xaProperties["user"] = user
        it.xaProperties["password"] = password
        it.xaProperties["url"] = url
    }

    @Bean
    fun skufdbJdbcTemplate(@Qualifier("skufdbAtomikosDataSource") skufdbDataSource: DataSource) =
        NamedParameterJdbcTemplate(skufdbDataSource)
}
