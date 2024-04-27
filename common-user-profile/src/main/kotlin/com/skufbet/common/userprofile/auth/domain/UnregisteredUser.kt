package com.skufbet.common.userprofile.auth.domain

data class UnregisteredUser(
    val keycloakId: String
): User
