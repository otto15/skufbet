package com.skufbet.core.api.graphql.controller.userprofile

import com.skufbet.common.userprofile.auth.domain.AuthenticatedUser
import com.skufbet.common.userprofile.auth.domain.UnregisteredUser
import com.skufbet.core.api.auth.AuthHelper
import com.skufbet.core.api.bet.dto.GetUserProfileTo
import com.skufbet.core.api.client.userprofile.UserProfileApiClient
import com.skufbet.core.api.client.userprofile.dto.CreateUserProfileRequestTo
import com.skufbet.core.api.client.userprofile.dto.ProfileIdTo
import com.skufbet.core.api.graphql.model.userprofile.UserProfile
import com.skufbet.core.api.graphql.model.userprofile.mutation.UserProfileMutation
import com.skufbet.core.api.graphql.model.userprofile.mutation.input.UserProfileCreateInput
import com.skufbet.core.api.graphql.model.userprofile.mutation.payload.UserProfileCreatePayload
import graphql.schema.DataFetchingEnvironment
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import org.springframework.web.client.HttpClientErrorException

@Controller
class UserProfileController(
    private val userProfileApiClient: UserProfileApiClient
) {
    @QueryMapping("userProfile")
    fun getUserProfile(env: DataFetchingEnvironment): UserProfile? =
        try {
            val user: AuthenticatedUser = AuthHelper.checkAndGetAuthenticatedUser(env)
            userProfileApiClient.getUserProfile(GetUserProfileTo(user.id))?.let {
                UserProfile(it.id, it.balance)
            }
        } catch (e: HttpClientErrorException) {
            null
        }

    @MutationMapping("userProfile")
    fun userProfileMutation() = UserProfileMutation()

    @SchemaMapping("create")
    fun createProfile(
        env: DataFetchingEnvironment,
        userProfileMutation: UserProfileMutation,
        @Argument("userProfileCreateInput") userProfileCreateInput: UserProfileCreateInput
    ): UserProfileCreatePayload {
        val user: UnregisteredUser = AuthHelper.checkAndGetUnregisteredUser(env)

        return userProfileApiClient
            .createUserProfile(userProfileCreateInput.toDto(user.keycloakId))
            .toPayload()
    }

    fun UserProfileCreateInput.toDto(keycloakId: String) = CreateUserProfileRequestTo(
        keycloakId,
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
