package com.skufbet.core.api.userprofile.service

import com.skufbet.core.api.userprofile.dao.UserProfileDao
import com.skufbet.core.api.userprofile.dao.UserProfileDetailsDao
import com.skufbet.core.api.userprofile.domain.UserProfile
import com.skufbet.core.api.userprofile.domain.UserProfileDetails
import com.skufbet.core.api.userprofile.service.command.UserProfileCreateCommand
import com.skufbet.skufdb.id.IdGenerator
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.transaction.annotation.Transactional

open class UserProfileCreationService(
    private val userProfileIdGenerator: IdGenerator<Int>,
    private val userProfileDao: UserProfileDao,
    private val userProfileDetailsDao: UserProfileDetailsDao,
    private val passwordEncoder: PasswordEncoder,
) {
    @Transactional
    open fun create(userProfileCreateCommand: UserProfileCreateCommand): UserProfile {
        val userProfile = UserProfile(
            userProfileIdGenerator.generate(),
            userProfileCreateCommand.mail,
            userProfileCreateCommand.phoneNumber,
            passwordEncoder.encode(userProfileCreateCommand.password),
            0
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
}
