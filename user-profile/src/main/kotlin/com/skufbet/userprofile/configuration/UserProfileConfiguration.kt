package com.skufbet.userprofile.configuration


import com.atomikos.icatch.jta.UserTransactionManager
import com.skufbet.userprofile.dao.UserProfileDao
import com.skufbet.userprofile.dao.UserProfileDetailsDao
import com.skufbet.userprofile.service.UserProfileCreationService
import com.skufbet.utils.database.id.IdGenerator
import com.skufbet.utils.database.id.PgSequenceIdGenerator
import jakarta.transaction.TransactionManager
import jakarta.transaction.UserTransaction
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.transaction.jta.JtaTransactionManager
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
class UserProfileConfiguration {
    @Bean
    fun encoder() = BCryptPasswordEncoder()

    @Bean
    fun userTransactionManager() = UserTransactionManager()

    @Bean
    fun jtaTransactionManager(): JtaTransactionManager {
        val tm = JtaTransactionManager()
        tm.transactionManager = userTransactionManager()
        tm.userTransaction = userTransactionManager()
        return tm
    }


    @Bean
    fun userProfileDao(@Qualifier("userProfileJdbcTemplate") jdbcTemplate: NamedParameterJdbcTemplate) =
        UserProfileDao(jdbcTemplate)

    @Bean
    fun userProfileDetailsDao(@Qualifier("skufdbJdbcTemplate") jdbcTemplate: NamedParameterJdbcTemplate) =
        UserProfileDetailsDao(jdbcTemplate)

    @Bean
    fun userProfileCreationService(
        @Qualifier("userProfileIdGenerator") idGenerator: IdGenerator<Int>,
        @Qualifier("userProfileDao") userProfileDao: UserProfileDao,
        @Qualifier("userProfileDetailsDao") userProfileDetailsDao: UserProfileDetailsDao,
        @Qualifier("encoder") passwordEncoder: PasswordEncoder,
        @Qualifier("userProfileAtomikosDataSource") userProfileDataSource: DataSource,
        @Qualifier("skufdbAtomikosDataSource") skufdbDataSource: DataSource
    ) = UserProfileCreationService(idGenerator, userProfileDao, userProfileDetailsDao, passwordEncoder, userProfileDataSource, skufdbDataSource)

    @Bean
    fun userProfileIdGenerator(@Qualifier("userProfileJdbcTemplate") jdbcTemplate: NamedParameterJdbcTemplate) =
        PgSequenceIdGenerator(USER_PROFILE_ID_SEQ, jdbcTemplate)

    companion object {
        private const val USER_PROFILE_ID_SEQ = "user_profile_id_seq"
    }
}
