package com.skufbet.core.api.bet.dto

import com.skufbet.common.userprofile.domain.UserProfileRole

data class GetUserProfileResponse(
    val id: Int,
    val keycloakId: String,
    val mail: String,
    val phoneNumber: String,
    val password: String,
    val balance: Int,
    val role: UserProfileRole
)
