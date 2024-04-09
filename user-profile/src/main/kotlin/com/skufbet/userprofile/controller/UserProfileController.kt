package com.skufbet.userprofile.controller

import com.skufbet.userprofile.domain.UserProfile
import com.skufbet.userprofile.dto.CreateUserProfileRequestTo
import com.skufbet.userprofile.dto.ProfileIdTo
import com.skufbet.userprofile.service.UserProfileCreationService
import com.skufbet.userprofile.service.command.UserProfileCreateCommand
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserProfileController(
    private val userProfileCreationService: UserProfileCreationService,
) {
    @PostMapping("/user-profiles")
    fun create(@RequestBody createUserProfileRequestTo: CreateUserProfileRequestTo): ProfileIdTo {
        return userProfileCreationService
            .create(createUserProfileRequestTo.toCommand())
            .toDto()
    }

    fun CreateUserProfileRequestTo.toCommand() = UserProfileCreateCommand(
        this.mail,
        this.phoneNumber,
        this.password,
        this.firstName,
        this.lastName,
        this.passport,
        this.dateOfBirth,
        this.taxPayerId,
    )

    fun UserProfile.toDto() = ProfileIdTo(this.id)
}
