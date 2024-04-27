package com.skufbet.common.userprofile.auth.domain

import com.skufbet.common.userprofile.domain.UserProfilePermission

data class AuthenticatedUser(
    val id: Int,
    val keycloakId: String,
    val permissions: Set<UserProfilePermission>,
): User
