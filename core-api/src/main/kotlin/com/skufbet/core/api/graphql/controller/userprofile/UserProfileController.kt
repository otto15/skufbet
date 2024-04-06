package com.skufbet.core.api.graphql.controller.userprofile

import com.skufbet.core.api.graphql.model.userprofile.mutation.UserProfileMutation
import com.skufbet.core.api.graphql.model.userprofile.mutation.input.UserProfileCreateInput
import com.skufbet.core.api.graphql.model.userprofile.mutation.payload.UserProfileCreatePayload
import com.skufbet.core.api.userprofile.domain.UserProfile
import com.skufbet.core.api.userprofile.service.UserProfileCreationService
import com.skufbet.core.api.userprofile.service.command.UserProfileCreateCommand
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
class UserProfileController(
    private val userProfileCreationService: UserProfileCreationService
) {
    @MutationMapping("userProfile")
    fun userProfileMutation() = UserProfileMutation()

    @SchemaMapping("create")
    fun createProfile(
        userProfileMutation: UserProfileMutation,
        @Argument("userProfileCreateInput") userProfileCreateInput: UserProfileCreateInput
    ): UserProfileCreatePayload {
        return userProfileCreationService
            .create(userProfileCreateInput.toCommand())
            .toPayload()
    }

    fun UserProfileCreateInput.toCommand() = UserProfileCreateCommand(
        this.mail,
        this.phoneNumber,
        this.password,
        this.firstName,
        this.lastName,
        this.passport,
        this.dateOfBirth,
        this.taxPayerId
    )

    fun UserProfile.toPayload() = UserProfileCreatePayload(this.id)
}
