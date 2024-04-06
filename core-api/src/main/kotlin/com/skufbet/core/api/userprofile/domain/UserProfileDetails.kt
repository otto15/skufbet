package com.skufbet.core.api.userprofile.domain

import java.util.Date

data class UserProfileDetails(
    val userProfileId: Int,
    val firstName: String,
    val lastName: String,
    val passport: String,
    val dateOfBirth: Date,
    val taxpayerId: String,
)
