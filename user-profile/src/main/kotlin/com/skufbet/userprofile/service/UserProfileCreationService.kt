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
