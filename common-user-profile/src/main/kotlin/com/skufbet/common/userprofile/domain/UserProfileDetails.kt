package com.skufbet.common.userprofile.domain

import java.util.*

data class UserProfileDetails(
    val userProfileId: Int,
    val firstName: String,
    val lastName: String,
    val passport: String,
    val dateOfBirth: Date,
    val taxpayerId: String,
)
