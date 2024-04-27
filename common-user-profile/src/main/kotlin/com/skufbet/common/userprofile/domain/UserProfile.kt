package com.skufbet.common.userprofile.domain

data class UserProfile(
    val id: Int,
    val keycloakId: String,
    val mail: String,
    val phoneNumber: String,
    val password: String,
    val balance: Int,
    val role: UserProfileRole,
)
