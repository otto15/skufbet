package com.skufbet.userprofile.service

import com.skufbet.common.userprofile.domain.UserProfile
import com.skufbet.common.userprofile.domain.UserProfileDetails
import com.skufbet.common.userprofile.domain.UserProfileRole
import com.skufbet.userprofile.dao.UserProfileDao
import com.skufbet.userprofile.dao.UserProfileDetailsDao
import com.skufbet.userprofile.service.command.UserProfileCreateCommand
import com.skufbet.utils.database.id.IdGenerator
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.transaction.annotation.Transactional
import java.sql.Date
import javax.sql.DataSource

open class UserProfileCreationService(
    private val userProfileIdGenerator: IdGenerator<Int>,
    private val userProfileDao: UserProfileDao,
    private val userProfileDetailsDao: UserProfileDetailsDao,
    private val passwordEncoder: PasswordEncoder,
    private val userProfileDataSource: DataSource,
    private val skufdbDataSource: DataSource
) {
    @Transactional(transactionManager = "jtaTransactionManager")
    open fun create(userProfileCreateCommand: UserProfileCreateCommand): UserProfile {
        //todo rewrite for jdbc

//        val userProfile = userProfileDataSource.connection.use { upCon ->
//            skufdbDataSource.connection.use { sdbCon ->
//                val generateId = upCon.createStatement()
//                val idRs = generateId.executeQuery("""
//                    SELECT nextval('user_profile_id_seq')
//                """.trimIndent())
//                if (!idRs.next()) throw RuntimeException("broken sequence")
//                val userProfile = UserProfile(
//                    idRs.getInt(1),
//                    userProfileCreateCommand.mail,
//                    userProfileCreateCommand.phoneNumber,
//                    passwordEncoder.encode(userProfileCreateCommand.password),
//                    0
//                )
//                val upInsert = upCon.prepareStatement("""
//                    INSERT INTO user_profile (id, mail, phone_number, password, balance)
//                    VALUES (?, ?, ?, ?, ?)
//                """.trimIndent())
//                upInsert.setInt(1, userProfile.id)
//                upInsert.setString(2, userProfile.mail)
//                upInsert.setString(3, userProfile.phoneNumber)
//                upInsert.setString(4, userProfile.password)
//                upInsert.setInt(5, userProfile.balance)
//                upInsert.execute()
//                upInsert.close()
//                val updInsert = sdbCon.prepareStatement("""
//                    INSERT INTO user_profile_details (user_profile_id, first_name, last_name, passport, date_of_birth, taxpayer_id)
//                    VALUES (?, ?, ?, ?, ?, ?);
//                """.trimIndent())
//                updInsert.setInt(1, userProfile.id)
//                updInsert.setString(2, userProfileCreateCommand.firstName)
//                updInsert.setString(3, userProfileCreateCommand.lastName)
//                updInsert.setString(4, userProfileCreateCommand.passport)
//                updInsert.setDate(5, Date(userProfileCreateCommand.dateOfBirth.time))
//                updInsert.setString(6, userProfileCreateCommand.taxPayerId)
//                updInsert.execute()
//                updInsert.close()
//                userProfile
//            }
//        }

        val userProfile = UserProfile(
            userProfileIdGenerator.generate(),
            userProfileCreateCommand.keycloakId,
            userProfileCreateCommand.mail,
            userProfileCreateCommand.phoneNumber,
            passwordEncoder.encode(userProfileCreateCommand.password),
            0,
            UserProfileRole.CLIENT
        )

        userProfileDao.insert(userProfile)
        userProfileDetailsDao.insert(
            UserProfileDetails(
                userProfile.id,
                userProfileCreateCommand.firstName,
                userProfileCreateCommand.lastName,
                userProfileCreateCommand.passport,
                userProfileCreateCommand.dateOfBirth,
                userProfileCreateCommand.taxPayerId
            )
        )

        return userProfile
    }

    open fun get(id: Int) = userProfileDao.selectBy(id)

    open fun getByKeycloakId(keycloakId: String) = userProfileDao.selectByKeycloakId(keycloakId)
}
