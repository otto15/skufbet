package com.skufbet.core.api.graphql.controller.userprofile

import com.skufbet.core.api.client.userprofile.UserProfileApiClient
import com.skufbet.core.api.client.userprofile.dto.CreateUserProfileRequestTo
import com.skufbet.core.api.client.userprofile.dto.ProfileIdTo
import com.skufbet.core.api.graphql.model.userprofile.mutation.UserProfileMutation
import com.skufbet.core.api.graphql.model.userprofile.mutation.input.UserProfileCreateInput
import com.skufbet.core.api.graphql.model.userprofile.mutation.payload.UserProfileCreatePayload
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
class UserProfileController(
    private val userProfileApiClient: UserProfileApiClient
) {
    @MutationMapping("userProfile")
    fun userProfileMutation() = UserProfileMutation()

    @SchemaMapping("create")
    fun createProfile(
        userProfileMutation: UserProfileMutation,
        @Argument("userProfileCreateInput") userProfileCreateInput: UserProfileCreateInput
    ): UserProfileCreatePayload {
        return userProfileApiClient
            .createUserProfile(userProfileCreateInput.toDto())
            .toPayload()
    }

    fun UserProfileCreateInput.toDto() = CreateUserProfileRequestTo(
        this.mail,
        this.phoneNumber,
        this.password,
        this.firstName,
        this.lastName,
        this.passport,
        this.dateOfBirth,
        this.taxPayerId,
    )

    fun ProfileIdTo.toPayload() = UserProfileCreatePayload(this.id)
}
