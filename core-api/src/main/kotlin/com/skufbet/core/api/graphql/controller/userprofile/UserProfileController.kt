package com.skufbet.core.api.graphql.controller.userprofile

import com.skufbet.core.api.graphql.model.userprofile.mutation.UserProfileMutation
import com.skufbet.core.api.graphql.model.userprofile.mutation.input.UserProfileCreateInput
import com.skufbet.core.api.graphql.model.userprofile.mutation.payload.UserProfileCreatePayload
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
class UserProfileController {
    @MutationMapping("userProfile")
    fun userProfileMutation() = UserProfileMutation()

    @SchemaMapping("create")
    fun createProfile(
        userProfileMutation: UserProfileMutation,
        @Argument("userProfileCreateInput") userProfileCreateInput: UserProfileCreateInput
    ): UserProfileCreatePayload {
        return UserProfileCreatePayload(1000)
    }
}