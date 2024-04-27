package com.skufbet.core.api.auth

import com.skufbet.common.userprofile.auth.domain.AuthenticatedUser
import com.skufbet.common.userprofile.auth.domain.UnregisteredUser
import com.skufbet.common.userprofile.auth.domain.User
import com.skufbet.common.userprofile.auth.exception.UnauthorizedException
import com.skufbet.common.userprofile.domain.UserProfilePermission
import graphql.schema.DataFetchingEnvironment

object AuthHelper {
    private const val AUTHORIZATION_ATTRIBUTE = "Authorization"

    fun checkAuthenticatedUser(
        env: DataFetchingEnvironment,
        permissions: Set<UserProfilePermission>
    ) {
        val user: User = env.graphQlContext[AUTHORIZATION_ATTRIBUTE]

        if (user !is AuthenticatedUser) {
            throw UnauthorizedException("Authenticated user is requested")
        }

        if (!user.permissions.containsAll(permissions.toSet())) {
            throw UnauthorizedException("Authenticated user does not have required permissions")
        }
    }

    fun checkAndGetAuthenticatedUser(
        env: DataFetchingEnvironment,
        permissions: Set<UserProfilePermission> = setOf()
    ): AuthenticatedUser {
        val user: User = env.graphQlContext[AUTHORIZATION_ATTRIBUTE]

        if (user !is AuthenticatedUser) {
            throw UnauthorizedException("Authenticated user is requested")
        }

        if (!user.permissions.containsAll(permissions.toSet())) {
            throw UnauthorizedException("Authenticated user does not have required permissions")
        }

        return user
    }

    fun checkAndGetUnregisteredUser(env: DataFetchingEnvironment): UnregisteredUser {
        val user: User = env.graphQlContext[AUTHORIZATION_ATTRIBUTE]

        if (user !is UnregisteredUser) {
            throw UnauthorizedException("Unregistered user is requested")
        }

        return user
    }
}