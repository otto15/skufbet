package com.skufbet.core.api.userprofile.service

import com.skufbet.core.api.userprofile.dao.UserProfileDao
import com.skufbet.core.api.userprofile.domain.UserProfile
import com.skufbet.core.api.userprofile.service.command.UserProfileCreateCommand
import com.skufbet.skufdb.id.IdGenerator
import org.springframework.security.crypto.password.PasswordEncoder

class UserProfileCreationService(
    private val idGenerator: IdGenerator<Int>,
    private val userProfileDao: UserProfileDao,
    private val passwordEncoder: PasswordEncoder,
) {
    fun create(userProfileCreateCommand: UserProfileCreateCommand): UserProfile {
        val userProfile = UserProfile(
            idGenerator.generate(),
            userProfileCreateCommand.mail,
            userProfileCreateCommand.phoneNumber,
            passwordEncoder.encode(userProfileCreateCommand.password),
            0
        )

        userProfileDao.insert(userProfile)

        return userProfile
    }
}
